package ru.src.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.src.model.entity.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
//    Flight findByFlight_id(Integer flightId);
}
