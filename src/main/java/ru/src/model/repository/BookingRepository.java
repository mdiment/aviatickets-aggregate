package ru.src.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.src.model.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByBookRef(String book_ref);
}
