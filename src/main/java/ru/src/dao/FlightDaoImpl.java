package ru.src.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.src.dto.Greeting;
import ru.src.dto.Flight;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class FlightDaoImpl implements FlightDao {

    private final static String FIND_TICKETS =
            "select flight_no, \n" +
            "   scheduled_departure, \n" +
            "   scheduled_arrival \n" +
            "from bookings.flights \n" +
            "where departure_airport = :from \n" +
            "   and arrival_airport = :to \n";

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

        for(Map<String, Object> row : queryResult){
            Flight flight = new Flight();

            String ticketNum = (String) row.get("flight_no");
            flight.setFlightNum(ticketNum);

            Timestamp arrival = (Timestamp) row.get("scheduled_arrival");
            flight.setArrival(arrival);

            Timestamp departure = (Timestamp) row.get("scheduled_departure");
            flight.setDeparture(departure);

            result.add(flight);
        }
        return result;
    }
}
