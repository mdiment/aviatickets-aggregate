package ru.src.dao;

import ru.src.dto.FlightInfo;
import ru.src.dto.Search;
import ru.src.dto.Flight;
import ru.src.model.entity.User;

import java.util.List;

public interface FlightDao {
    List<Flight> getFlights(Search search);

    FlightInfo getTickets(Integer flightId);

    void makeOrder(Integer flightId, String fareConditions, User user);
}
