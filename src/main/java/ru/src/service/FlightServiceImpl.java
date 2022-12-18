package ru.src.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.src.dao.FlightDao;
import ru.src.dto.FlightDto;
import ru.src.dto.FlightInfo;
import ru.src.dto.SearchDto;
import ru.src.model.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightDao flightDao;

    @Override
    public List<FlightDto> getFlights(SearchDto searchDto) {
        if (searchDto.getDate() == null){
            searchDto.setDate(LocalDateTime.now());
        }
        return flightDao.getFlights(searchDto);
    }
    public FlightInfo getTickets(Integer flightId){
        return flightDao.getTickets(flightId);
    }

    @Override
    public void makeOrder(Integer flightId, String fareConditions, User user) {
        flightDao.makeOrder(flightId, fareConditions, user);
    }

}
