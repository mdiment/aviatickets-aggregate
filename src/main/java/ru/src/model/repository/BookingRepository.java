package ru.src.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.src.model.entity.Booking;
import ru.src.model.entity.User;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByBookRef(String book_ref);

    void deleteByBookRef(String book_ref);

    List<Booking> findAllByUser(User user);
}
