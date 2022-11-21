package ru.src.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {

    private Integer id;
    private String flightNum;
    private Timestamp arrival;
    private Timestamp departure;

}
