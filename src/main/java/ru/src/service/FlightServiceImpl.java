package ru.src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.src.dao.FlightDao;
import ru.src.dto.Greeting;
import ru.src.dto.Flight;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Override
    public List<Flight> geFlights(Greeting greeting) {
        return flightDao.getFlights(greeting);
    }
}
