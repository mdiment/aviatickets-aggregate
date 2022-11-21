package ru.src.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.src.configuration.SessionContext;
import ru.src.dto.Flight;
import ru.src.dto.FlightInfo;
import ru.src.dto.Greeting;
import ru.src.dto.Ticket;
import ru.src.service.FlightService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

//import javax.annotation.PostConstruct;


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
    public String index1(Model model){
//        String ans = "Hello!";
//        if (sessionContext.getUser() != null){
//            ans = "Hello, " + sessionContext.getUser().getEmail() + "!";
//        }
        model.addAttribute("user", sessionContext.getUser());
        model.addAttribute("greeting", new Greeting());
        return "index_aviatickets";
    }

//    @GetMapping("/registration")
//    public String getRegistration(Model model) {
//        model.addAttribute("user", new User());
//        return "registration";
//    }

    @PostMapping("/flights")
    public ModelAndView getTickets(@ModelAttribute("flight") Greeting greeting) {
        ModelAndView view = new ModelAndView("index_aviatickets");
        List<Flight> flights = flightService.getFlights(greeting);
        view.addObject("greeting", greeting);
        view.addObject("flights", flights);
        view.addObject("user", sessionContext.getUser());
        return view;
    }

    @GetMapping("/order")
    public String makeOrder() {
        ModelAndView view = new ModelAndView("index_aviatickets");
        if(sessionContext.getUser() == null){
            return "redirect:/login?flightId";
        }
        return "redirect:/";
    }

    @RequestMapping("/flight-info/{flightId}")
    public ModelAndView getFlightInfo(@PathVariable Integer flightId){
        log.info("info about: " + flightId.toString());
//        HttpServletResponse httpServletResponse;
//        httpServletResponse.addCookie(new Cookie("dskjdksjd", "slkdlsklds"));
        ModelAndView view = new ModelAndView("flight");
        FlightInfo flightInfo = flightService.getTickets(flightId);
        view.addObject("user", sessionContext.getUser());
        view.addObject("flightInfo", flightInfo);
        return view;
    }
//    @PostConstruct
//    public String sadas(){
////        System.out.println(2132);
//        return "123";
//    }
}
