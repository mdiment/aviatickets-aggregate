package ru.src.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Search {

    private String from;
    private String to;
    private String date;
//    private List<Flight> flightsList;

}
