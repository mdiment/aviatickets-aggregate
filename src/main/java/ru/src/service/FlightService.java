package ru.src.service;

import ru.src.dto.FlightInfo;
import ru.src.dto.Greeting;
import ru.src.dto.Flight;
import ru.src.model.entity.User;

import java.util.List;

public interface FlightService {

    List<Flight> getFlights(Greeting greeting);

    FlightInfo getTickets(Integer flightId);

    void makeOrder(Integer flightId, String fareConditions, User user);
}
