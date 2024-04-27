package io.teamchallenge.service;

import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.teamchallenge.constant.ExceptionMessage.USER_NOT_FOUND_BY_EMAIL;

/**
 * Service class for user authorization-related operations.
 */
@Service
@Transactional
public class UserAuthorisationService {
    private final UserRepository userRepository;

    /**
     * Constructor for UserAuthorisationService.
     *
     * @param userRepository The user repository.
     */
    @Autowired
    public UserAuthorisationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by email.
     *
     * @param email The email of the user to find.
     * @return The {@link UserVO} object representing the user.
     * @throws NotFoundException if the user with the specified email is not found.
     */
    @Transactional(readOnly = true)
    public UserVO findUserVOByEmail(String email) {
        return userRepository.findUserVOByEmail(email).stream().findAny()
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_BY_EMAIL));
    }
}