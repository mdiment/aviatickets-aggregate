package ru.src.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import ru.src.dto.FlightInfo;
import ru.src.dto.Greeting;
import ru.src.dto.Flight;
import ru.src.dto.Ticket;
import ru.src.model.entity.Booking;
import ru.src.model.entity.User;
import ru.src.model.repository.BookingRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Integer.parseInt;

@Slf4j
@Repository
public class FlightDaoImpl implements FlightDao {

    private final static String FIND_TICKETS =
            "select flight_id,\n" +
                    "   flight_no, \n" +
                    "   scheduled_departure, \n" +
                    "   scheduled_arrival, \n" +
                    "   scheduled_duration \n" +
                    "from bookings.flights_v \n" +
                    "where departure_city = :from \n" +
                    "   and arrival_city = :to \n" +
                    "   and status = 'Scheduled' \n" +
                    "order by scheduled_departure ASC, \n" +
                    "   scheduled_arrival desc, \n" +
                    "   flight_id \n" +
                    "limit 20 \n";

    private final static String FIND_SEATS_LEFT =
                "select flight_id, \n" +
                    "   fare_conditions, \n" +
                    "   left_seats \n" +
                    "from bookings.ticket_filling\n" +
                    "where flight_id = :flightId";

    private final static String FIND_FLIGHT =
            "select flight_id, \n" +
                    "   model \n" +
                    "from bookings.flight_info\n" +
                    "where flight_id = :flightId";

    @Autowired
    NamedParameterJdbcOperations jdbcOperations;

    @Autowired
    BookingRepository bookingRepository;

    @Override
    public List<Flight> getFlights(Greeting greeting) {
        log.info(greeting.toString());

        Map<String, Object> params = new HashMap<>();
        params.put("from", greeting.getFrom());
        params.put("to", greeting.getTo());

        List<Map<String, Object>> queryResult = jdbcOperations.queryForList(FIND_TICKETS, params);
        List<Flight> result = new ArrayList<>();

        for (Map<String, Object> row : queryResult) {
            Flight flight = new Flight();

            Integer flightId = (Integer) row.get("flight_id");
            flight.setId(flightId);

            String flightNum = (String) row.get("flight_no");
            flight.setFlightNum(flightNum);

            Timestamp arrival = (Timestamp) row.get("scheduled_arrival");
            flight.setArrival(arrival);

            Timestamp departure = (Timestamp) row.get("scheduled_departure");
            flight.setDeparture(departure);

//            PGInterval duration;
//            try {
//                log.info(row.get("scheduled_duration").toString());
            String duration = row.get("scheduled_duration").toString();
//            }
//            catch(java.sql.SQLException exception){
//                duration = new PGInterval();
//                log.info(exception.toString());
//            }
            flight.setDuration(duration);

            result.add(flight);
        }
        return result;
    }

    public FlightInfo getTickets(Integer flightId) {
        log.info("flightId: " + flightId);

        Map<String, Object> params = new HashMap<>();
        params.put("flightId", flightId);

        List<Map<String, Object>> queryResult = jdbcOperations.queryForList(FIND_SEATS_LEFT, params);
        FlightInfo flightInfo = new FlightInfo();
        List<Ticket> result = new ArrayList<>();
        if (queryResult.isEmpty()){
            flightInfo.setTickets(Collections.emptyList());
        }

        for (Map<String, Object> row : queryResult) {
            Ticket ticket = new Ticket();

            Integer count = (Integer) row.get("left_seats");
            ticket.setCount(count);

            String condition = (String) row.get("fare_conditions");
            ticket.setFareConditions(condition != null ? condition : "Economy");

            result.add(ticket);
        }
        flightInfo.setTickets(result);
        List<Map<String, Object>> queryModel = jdbcOperations.queryForList(FIND_FLIGHT, params);
        flightInfo.setFlightId(parseInt(queryModel.get(0).get("flight_id").toString()));
        flightInfo.setModel(queryModel.get(0).get("model").toString());
        return flightInfo;
    }

    @Override
    public void makeOrder(Integer flightId, String fareConditions, User user) {
        log.info(flightId + " fare " + fareConditions);
        String book_ref = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        double amount = Objects.equals(fareConditions, "Economy") ? 3000.00 : 18000.00;
        Booking booking = new Booking(book_ref,
                Timestamp.valueOf(LocalDateTime.now()),
                amount,
                user.getId());
        user.getBookings().add(booking);
        bookingRepository.save(booking);
        Map<String, Object> params_ticket = new HashMap<>();
        String ticket_no = UUID.randomUUID().toString().substring(0, 13).toUpperCase();
        params_ticket.put("ticket_no", ticket_no);
        params_ticket.put("book_ref", book_ref);
        params_ticket.put("passenger_id", user.getId());
        params_ticket.put("passenger_name", user.getEmail());
        jdbcOperations.update("INSERT INTO bookings.tickets (ticket_no, book_ref, passenger_id, passenger_name)\n" +
                "VALUES(:ticket_no, :book_ref, :passenger_id, :passenger_name);",
                params_ticket);
        params_ticket.put("flight_id", flightId);
        params_ticket.put("fare_conditions", fareConditions);
        params_ticket.put("amount", amount);
        jdbcOperations.update("INSERT INTO ticket_flights (ticket_no, flight_id, fare_conditions, amount)\n" +
                        "        VALUES      (:ticket_no, :flight_id, :fare_conditions, :amount);",
                params_ticket);
    }
}
