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
import ru.src.dto.OrderDto;
import ru.src.dto.UserDto;
import ru.src.model.entity.Booking;
import ru.src.model.entity.Ticket;
import ru.src.model.entity.User;
import ru.src.service.AuthService;
import ru.src.service.UserService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static ru.src.util.DateUtils.DATE_FORMAT;

@Controller(value = "registration")
@Slf4j
@Scope("session")
@AllArgsConstructor
/**
 * It's a controller that handles all the requests related to user authentication
 */
public class AuthController {

    private final UserService userService;

    private final AuthService ticketService;
    @Autowired
    SessionContext sessionContext;

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
        List<Booking> bookings = new ArrayList<>(sessionContext.getUser().getBookings());
        List<OrderDto> orders = new ArrayList<>();
        for(Booking booking: bookings){
            log.info(DATE_FORMAT.format(booking.getBook_date()));
        }
        for(Booking booking: sessionContext.getUser().getBookings()){
            OrderDto orderDto = new OrderDto();
            orderDto.setBookRef(booking.getBookRef());
            orderDto.setTotalAmount(booking.getTotal_amount());
            for(Ticket ticket : booking.getTickets()){
//                Ticket ticketForOrder = new Ticket();
                orderDto.setDepAirport(ticket.getFlight().getDeparture_airport());
                orderDto.setArrAirport(ticket.getFlight().getArrival_airport());
                orderDto.setDepTime(DATE_FORMAT.format(Timestamp.valueOf(ticket.getFlight().getScheduledDeparture().toLocalDateTime())));
                orderDto.setArrTime(DATE_FORMAT.format(Timestamp.valueOf(ticket.getFlight().getScheduled_arrival().toLocalDateTime())));
            }
            orders.add(orderDto);
        }
        model.addAttribute("orders", orders);
        return "personal";
    }

    /**
     *
     * @param user пользователь, по которому делается выборка бронирований
     * @return список бронирований, форматированный для представления
     */
    private List<OrderDto> listOrders(User user){
        List<OrderDto> orders = new ArrayList<>();
        for(Booking booking: user.getBookings()){
            OrderDto orderDto = new OrderDto();
            orderDto.setBookRef(booking.getBookRef());
            orderDto.setTotalAmount(booking.getTotal_amount());
            for(Ticket ticket : booking.getTickets()){
                orderDto.setDepAirport(ticket.getFlight().getDeparture_airport());
                orderDto.setArrAirport(ticket.getFlight().getArrival_airport());
                orderDto.setDepTime(DATE_FORMAT.format(Timestamp.valueOf(ticket.getFlight().getScheduledDeparture().toLocalDateTime())));
                orderDto.setArrTime(DATE_FORMAT.format(Timestamp.valueOf(ticket.getFlight().getScheduled_arrival().toLocalDateTime())));
            }
            orders.add(orderDto);
        }
        return orders;
    }
    /**
     * @param bookRef бронирование для удаления
     * @param model интерфейс для передачи данных между представлениями
     * @return String имя представления для отображения
     */
    @RequestMapping("/personal/delete/{bookRef}")
    public String deleteOrder(@PathVariable String bookRef,
                        Model model){
        // логирование
        log.info("delete bookRef: " + bookRef);
        // попытка удаления бронирования пользователем
        sessionContext.setUser(ticketService.deleteOrderFromUser(sessionContext.getUser(), bookRef));
        // наполнение ответа атрибутами, использующимися на стороне представления
        model.addAttribute("user", sessionContext.getUser());
        model.addAttribute("orders", listOrders(sessionContext.getUser()));
        // передача имени представления для ответа
        return "personal";
    }
}
