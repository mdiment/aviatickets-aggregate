package ru.src.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.src.configuration.SessionContext;
import ru.src.model.entity.User;
import ru.src.model.repository.UserRepository;

import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    SessionContext sessionContext;
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.info("load user");
        User user = userRepository.findByEmail(usernameOrEmail);
        if(user != null){
            sessionContext.setUser(user);
            return new org.springframework.security.core.userdetails.User(user.getEmail()
                    , user.getPassword(),
                    user.getRoles().stream()
                            .map((role) -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList()));
        }else {
            throw new UsernameNotFoundException("Invalid email or password");
        }
    }
}
