package ru.src.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.src.configuration.SessionContext;
import ru.src.dto.UserDto;
import ru.src.model.entity.User;
import ru.src.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller(value = "registration")
@Slf4j
@Scope("session")
public class AuthController {

    private final UserService userService;
    @Autowired
    SessionContext sessionContext;
    public AuthController(UserService userService) {
        this.userService = userService;
    }

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
    public String login(@RequestParam Integer flightId){
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

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

}
