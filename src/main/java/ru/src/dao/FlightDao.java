package ru.src.dao;

import ru.src.dto.FlightInfo;
import ru.src.dto.SearchDto;
import ru.src.dto.FlightDto;
import ru.src.model.entity.User;

import java.util.List;

public interface FlightDao {
    List<FlightDto> getFlights(SearchDto searchDto);

    FlightInfo getTickets(Integer flightId);

    void makeOrder(Integer flightId, String fareConditions, User user);
}
