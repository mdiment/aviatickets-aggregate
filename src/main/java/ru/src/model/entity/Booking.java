package ru.src.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "bookings")
public class Booking {
    @Id
    @Column(name = "book_ref", nullable = false)
    private String bookRef;

    @Column
    private Timestamp book_date;

    @Column
    private double total_amount;

    @ManyToOne//(fetch = FetchType.LAZY)
    private User user;

//    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();

}
