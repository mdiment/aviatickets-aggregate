package ru.src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.src.dao.FlightDao;
import ru.src.dto.FlightInfo;
import ru.src.dto.Greeting;
import ru.src.dto.Flight;
import ru.src.model.entity.User;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Override
    public List<Flight> getFlights(Greeting greeting) {
        return flightDao.getFlights(greeting);
    }
    public FlightInfo getTickets(Integer flightId){
        return flightDao.getTickets(flightId);
    }

    @Override
    public void makeOrder(Integer flightId, String fareConditions, User user) {
        flightDao.makeOrder(flightId, fareConditions, user);
    }

    ;

}
