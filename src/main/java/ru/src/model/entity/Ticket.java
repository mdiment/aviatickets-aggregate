package ru.src.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "tickets", schema = "bookings")
@NoArgsConstructor
public class Ticket {
    @Id
    @Column
    private String ticket_no;

    @Column
    private String passenger_id;

    @Column
    private String passenger_name;

    @Column
    private String fare_conditions;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Booking booking;

//    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ticket_flights_new",
            schema = "bookings",
            joinColumns = { @JoinColumn(name = "ticket_no", referencedColumnName = "ticket_no") },
            inverseJoinColumns = { @JoinColumn(name = "flight_id", referencedColumnName = "flight_id") }
    )
    private Flight flight;

}
