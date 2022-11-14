package ru.src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.src.dto.Greeting;
import ru.src.dto.User;
import ru.src.service.FlightService;

//import javax.annotation.PostConstruct;


@Controller
public class FlightsController {

    private final FlightService flightService;

    public FlightsController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/")
    public String index1(Model model){
        model.addAttribute("greeting", new Greeting());
        return "index_aviatickets";
    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/flights")
    public ModelAndView getTickets(@ModelAttribute("flight") Greeting greeting) {
        ModelAndView view = new ModelAndView("index_aviatickets");
        greeting.setFlightsList(flightService.geFlights(greeting));
        view.addObject("greeting", greeting);
        return view;
    }

//    @PostConstruct
//    public String sadas(){
////        System.out.println(2132);
//        return "123";
//    }
}
