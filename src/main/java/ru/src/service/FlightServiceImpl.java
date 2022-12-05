package ru.src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.src.dao.FlightDao;
import ru.src.dto.FlightDto;
import ru.src.dto.FlightInfo;
import ru.src.dto.SearchDto;
import ru.src.model.entity.User;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Override
    public List<FlightDto> getFlights(SearchDto searchDto) {
        return flightDao.getFlights(searchDto);
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
