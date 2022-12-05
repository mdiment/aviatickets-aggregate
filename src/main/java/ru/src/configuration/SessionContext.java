package ru.src.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.src.model.entity.User;
import ru.src.model.repository.UserRepository;

@Data
@Component
@SessionScope
public class SessionContext {

    @Autowired
    UserRepository userRepository;

    private User user;

    public User getUser() {
//        if (user==null) return null;
//        return userRepository.findByEmail(user.getEmail());
        return user;
    }
}
