package ru.src.configuration;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.src.model.entity.User;

@Data
@Component
@SessionScope
public class SessionContext {
    private User user;
}
