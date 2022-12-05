package ru.src.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Dashboard {
    private List<UserWithBookingsDto> statistics;
}
