package ru.src.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.src.dto.FlightInfo;
import ru.src.dto.Greeting;
import ru.src.dto.Flight;
import ru.src.dto.Ticket;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Repository
public class FlightDaoImpl implements FlightDao {

    private final static String FIND_TICKETS =
            "select flight_id,\n" +
                    "   flight_no, \n" +
                    "   scheduled_departure, \n" +
                    "   scheduled_arrival \n" +
                    "from bookings.flights \n" +
                    "where departure_airport = :from \n" +
                    "   and arrival_airport = :to \n" +
                    "order by scheduled_departure desc, \n" +
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

        flightInfo.setModel(queryModel.get(0).get("model").toString());
        return flightInfo;
    }
}
