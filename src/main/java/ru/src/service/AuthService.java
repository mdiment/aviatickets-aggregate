package ru.src.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.src.model.entity.Booking;
import ru.src.model.entity.User;
import ru.src.model.repository.BookingRepository;
import ru.src.model.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    /**
     * @param user пользователь, пытающийся удалить бронирование
     * @param bookRef бронирование для удаления
     * @return User - пользователь
     */
    public User deleteOrderFromUser(User user, String bookRef){
        // получение пользователя, владеющего бронированием
        Booking bookingToDelete = bookingRepository.findByBookRef(bookRef);
        User userToVerify = bookingToDelete.getUser();

        // проверка соответствии пользователя, совершающего удаление, и пользователя, владеющего бронированием
        if(userToVerify.equals(user)){
            // удаление бронирования
            userToVerify.getBookings().remove(bookingToDelete);
            // обновление БД
            userRepository.saveAndFlush(userToVerify);
            user = userToVerify;
        }

        // передача класс изменённого пользователя для изменения контекста сессии
        return user;
    }
}
