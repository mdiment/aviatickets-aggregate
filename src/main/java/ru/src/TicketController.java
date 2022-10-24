package ru.src;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Controller
public class TicketController {

    @GetMapping("/")
    public String index1(){
        System.out.println("234234d");
        return "index_aviatickets";
    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostConstruct
    public String sadas(){
//        System.out.println(2132);
        return "123";
    }
}
