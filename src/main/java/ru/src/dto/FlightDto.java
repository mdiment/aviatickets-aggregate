package ru.src.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {

    private Integer id;
    private String flightNum;
    private String arrival;
    private String departure;
    private String duration;

}
