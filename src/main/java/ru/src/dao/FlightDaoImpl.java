package ru.src.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.src.dto.FlightDto;
import ru.src.dto.FlightInfo;
import ru.src.dto.SearchDto;
import ru.src.dto.TicketDto;
import ru.src.model.entity.Booking;
import ru.src.model.entity.Flight;
import ru.src.model.entity.Ticket;
import ru.src.model.entity.User;
import ru.src.model.repository.BookingRepository;
import ru.src.model.repository.FlightRepository;
import ru.src.model.repository.UserRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Integer.parseInt;
import static ru.src.util.DateUtils.DATE_FORMAT;

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
                    "   and scheduled_departure::date >= :searchDate::date\n" +
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
    UserRepository userRepository;

    @Autowired
    FlightRepository flightRepository;

    @Override
    public List<FlightDto> getFlights(SearchDto searchDto) {
        log.info(searchDto.toString());

        Map<String, Object> params = new HashMap<>();
        params.put("from", searchDto.getFrom());
        params.put("to", searchDto.getTo());
        params.put("searchDate", searchDto.getDate());

        List<Map<String, Object>> queryResult = jdbcOperations.queryForList(FIND_TICKETS, params);
        List<FlightDto> result = new ArrayList<>();

        for (Map<String, Object> row : queryResult) {
            FlightDto flightDto = new FlightDto();

            Integer flightId = (Integer) row.get("flight_id");
            flightDto.setId(flightId);

            String flightNum = (String) row.get("flight_no");
            flightDto.setFlightNum(flightNum);

            Timestamp arrival = (Timestamp) row.get("scheduled_arrival");
            flightDto.setArrival(DATE_FORMAT.format(arrival));

            Timestamp departure = (Timestamp) row.get("scheduled_departure");
            flightDto.setDeparture(DATE_FORMAT.format(departure));

            String duration = row.get("scheduled_duration").toString();
            flightDto.setDuration(duration);

            result.add(flightDto);
        }
        return result;
    }

    public FlightInfo getTickets(Integer flightId) {
        log.info("flightId: " + flightId);

        Map<String, Object> params = new HashMap<>();
        params.put("flightId", flightId);

        List<Map<String, Object>> queryResult = jdbcOperations.queryForList(FIND_SEATS_LEFT, params);
        FlightInfo flightInfo = new FlightInfo();
        List<TicketDto> result = new ArrayList<>();
        if (queryResult.isEmpty()){
            flightInfo.setTicketDtos(Collections.emptyList());
        }

        for (Map<String, Object> row : queryResult) {
            TicketDto ticketDto = new TicketDto();

            Integer count = (Integer) row.get("left_seats");
            ticketDto.setCount(count);

            String condition = (String) row.get("fare_conditions");
            ticketDto.setFareConditions(condition != null ? condition : "Economy");

            result.add(ticketDto);
        }
        flightInfo.setTicketDtos(result);
        List<Map<String, Object>> queryModel = jdbcOperations.queryForList(FIND_FLIGHT, params);
        flightInfo.setFlightId(parseInt(queryModel.get(0).get("flight_id").toString()));
        flightInfo.setModel(queryModel.get(0).get("model").toString());
        return flightInfo;
    }

    @Override
    public void makeOrder(Integer flightId, String fareConditions, User user) {
        log.info(flightId + " fare " + fareConditions);
        Booking booking = new Booking();
        booking.setBook_date(Timestamp.valueOf(LocalDateTime.now()));
        booking.setBookRef(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        booking.setTotal_amount(Objects.equals(fareConditions, "Economy") ? 3000.00 : 18000.00);
        booking.setUser(user);
        Optional<Flight> flight = flightRepository.findById(Long.valueOf(flightId));
        Ticket ticket = new Ticket(
                UUID.randomUUID().toString().substring(0, 13).toUpperCase(),
                user.getId().toString(),
                user.getEmail(),
                fareConditions,
                booking,
                flight.get()
        );
        booking.getTickets().add(ticket);
        user.getBookings().add(booking);
        userRepository.saveAndFlush(user);
    }
}
