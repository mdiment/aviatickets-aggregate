package ru.src.dao;

import ru.src.dto.Greeting;
import ru.src.dto.Flight;

import java.util.List;

public interface FlightDao {
    public abstract List<Flight> getFlights(Greeting greeting);
}
