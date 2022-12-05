package ru.src.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.src.configuration.SessionContext;
import ru.src.dto.UserDto;
import ru.src.model.entity.Booking;
import ru.src.model.entity.User;
import ru.src.model.repository.BookingRepository;
import ru.src.model.repository.UserRepository;
import ru.src.service.UserService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller(value = "registration")
@Slf4j
@Scope("session")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

//    @Autowired
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Autowired
    SessionContext sessionContext;

//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }

    @GetMapping("/registration")
    public String index(){
        return "index_registration";
    }

    @GetMapping("/login")
    public String login(){
        log.info("just_login");
        return "login";
    }

    @RequestMapping("/login/{flightId}")
    public String login(@PathVariable Integer flightId){
        log.info("login_from_flight_info, flightId: " + flightId);
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        log.info("register");
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        log.info("register/save");
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "redirect:/register?error";
        }

        userService.saveUserAdmin(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/personal")
    public String showOrders(Model model){
        log.info("personal");
        if (sessionContext.getUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("user", sessionContext.getUser());
        model.addAttribute("orders", sessionContext.getUser().getBookings());
        return "personal";
    }

    @Transactional
    @RequestMapping("/personal/delete/{bookRef}")
    public String deleteOrder(@PathVariable String bookRef,
                        Model model){
        log.info("delete bookRef: " + bookRef);
        User user = sessionContext.getUser();
        if (user == null){
            return "redirect:/";
        }
        Booking booking = bookingRepository.findByBookRef(bookRef);
        User user1 = bookingRepository.findByBookRef(bookRef).getUser();
        boolean test = user1.equals(user);

        if(test){
            user1.getBookings().remove(booking);
            userRepository.saveAndFlush(user1);
            bookingRepository.delete(booking);
            sessionContext.setUser(user1);
        }

        model.addAttribute("user", user1);
        model.addAttribute("orders", user1.getBookings());
        return "personal";
    }
}
