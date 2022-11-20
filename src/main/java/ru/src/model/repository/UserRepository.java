package ru.src.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.src.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
