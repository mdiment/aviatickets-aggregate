package ru.src.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.src.configuration.SessionContext;
import ru.src.dto.Flight;
import ru.src.dto.FlightInfo;
import ru.src.dto.Search;
import ru.src.service.FlightService;

import java.util.List;

@Controller
@Slf4j
public class FlightsController {

    private final FlightService flightService;
    @Autowired
    SessionContext sessionContext;

    public FlightsController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/")
    public ModelAndView index1(){
        ModelAndView modelAndView = new ModelAndView("index_aviatickets");
        modelAndView.addObject("user", sessionContext.getUser());
        modelAndView.addObject("search", new Search());
        return modelAndView;
    }

    @PostMapping("/flights")
    public ModelAndView getFlights(@ModelAttribute("flight") Search search) {
        ModelAndView modelAndView = new ModelAndView("index_aviatickets");
        List<Flight> flights = flightService.getFlights(search);
        modelAndView.addObject("search", search);
        modelAndView.addObject("flights", flights);
        modelAndView.addObject("user", sessionContext.getUser());
        return modelAndView;
    }

    @RequestMapping("/order/{flightId}/{fareConditions}")
    public String makeOrder(@PathVariable Integer flightId,
                            @PathVariable String fareConditions) {
        log.info("order " + flightId.toString() + " fare conditions " + fareConditions);
        if(sessionContext.getUser() == null){
            return "redirect:/login/" + flightId;
        }
        if(sessionContext.getUser().getBookings() != null && sessionContext.getUser().getBookings().size() <= 5){
            flightService.makeOrder(flightId, fareConditions, sessionContext.getUser());
            return "redirect:/personal";
        }
        else{
            return "redirect:/flight-info/" + flightId + "?error";
        }


    }

    @RequestMapping("/flight-info/{flightId}")
    public ModelAndView getFlightInfo(@PathVariable Integer flightId){
        log.info("info about: " + flightId.toString());
        ModelAndView view = new ModelAndView("flight");
        FlightInfo flightInfo = flightService.getTickets(flightId);
        view.addObject("user", sessionContext.getUser());
        view.addObject("flightInfo", flightInfo);

        return view;
    }


}
