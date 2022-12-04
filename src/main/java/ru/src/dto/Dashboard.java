package ru.src.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.src.model.entity.Booking;
import ru.src.model.entity.User;

import java.util.List;

@Data
@AllArgsConstructor
public class Dashboard {
    private List<UserWithBookings> statistics;
}
