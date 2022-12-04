package ru.src.service;

import ru.src.dto.UserDto;
import ru.src.model.entity.User;

import java.util.List;

public interface UserService {
    void saveUserAdmin(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
