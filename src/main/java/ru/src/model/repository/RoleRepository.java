package ru.src.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.src.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
