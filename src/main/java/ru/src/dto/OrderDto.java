package ru.src.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDto {
    private String bookRef;
    private double totalAmount;
    private String depAirport;
    private String arrAirport;
    private String depTime;
    private String arrTime;
}
