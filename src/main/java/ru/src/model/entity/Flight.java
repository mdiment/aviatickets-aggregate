package ru.src.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "flights", schema = "bookings")
public class Flight {
    @Id
    private Long flight_id;

    @Column(insertable=false, updatable = false)
    private String flightNo;

    @Column(insertable=false, updatable = false)
    private ZonedDateTime scheduledDeparture;

    @Column(insertable=false, updatable = false)
    private ZonedDateTime scheduled_arrival;

    @Column(insertable=false, updatable = false)
    private String departure_airport;

    @Column(insertable=false, updatable = false)
    private String arrival_airport;

    @Column(insertable=false, updatable = false)
    private String status;

    @Column(insertable=false, updatable = false)
    private String aircraft_code;

    @Column(insertable=false, updatable = false)
    private ZonedDateTime actual_departure;

    @Column(insertable=false, updatable = false)
    private ZonedDateTime actual_arrival;

}
