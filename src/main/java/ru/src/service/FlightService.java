package ru.src.service;

import ru.src.dto.Greeting;
import ru.src.dto.Flight;

import java.util.List;

public interface FlightService {

    List<Flight> geFlights(Greeting greeting);
}
