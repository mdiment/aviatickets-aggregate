package ru.src.dao;

import ru.src.dto.FlightInfo;
import ru.src.dto.Greeting;
import ru.src.dto.Flight;
import ru.src.dto.Ticket;

import java.util.List;

public interface FlightDao {
    List<Flight> getFlights(Greeting greeting);

    FlightInfo getTickets(Integer flightId);
}
