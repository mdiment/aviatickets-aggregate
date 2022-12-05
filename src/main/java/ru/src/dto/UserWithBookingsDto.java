package ru.src.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.src.model.entity.Booking;
import ru.src.model.entity.User;

import java.util.List;

@Data
@AllArgsConstructor
public class UserWithBookingsDto {
    private User user;
    private List<String> roles;
    private List<Booking> bookings;
}
