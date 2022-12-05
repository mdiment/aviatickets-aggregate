package ru.src.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.src.configuration.SessionContext;
import ru.src.dto.Dashboard;
import ru.src.dto.UserWithBookingsDto;
import ru.src.model.entity.Role;
import ru.src.model.entity.User;
import ru.src.model.repository.BookingRepository;
import ru.src.model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    SessionContext sessionContext;

    @GetMapping("/dashboard")
    public ModelAndView formDashboard(){
        log.info("dashboard");
        ModelAndView modelAndView = new ModelAndView("dashboard");
        modelAndView.addObject("user", sessionContext.getUser());

        List<User> users = userRepository.findAll();
        List<UserWithBookingsDto> statistics = new ArrayList<>();
        for(User user : users){
            List<String> roles = new ArrayList<>();
            for (Role role : user.getRoles())
                roles.add(role.getName());

            UserWithBookingsDto userWithBookingsDto = new UserWithBookingsDto(user,
                    roles,
                    bookingRepository.findAllByUser(user)
            );
            statistics.add(userWithBookingsDto);
        }
        Dashboard dashboard = new Dashboard(statistics);

        modelAndView.addObject("statistics", dashboard.getStatistics());
//        modelAndView.addObject("bookings", adminService.findAllBookings());
        return modelAndView;
    }
}
