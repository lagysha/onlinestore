package io.teamchallenge.service;

import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.USER_NOT_FOUND_BY_EMAIL;

@Service
@Transactional
public class UserAuthorisationService {
    private final UserRepository userRepository;

    @Autowired
    public UserAuthorisationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserVO findUserVOByEmail(String email) {
        return userRepository.findUserVOByEmail(email).stream().findAny()
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_BY_EMAIL));
    }
}
