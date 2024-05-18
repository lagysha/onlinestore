package io.teamchallenge.service;

import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.exception.NotFoundException;
import io.teamchallenge.repository.UserRepository;
import io.teamchallenge.util.Utils;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAuthorizationServiceTest {
    @InjectMocks
    private UserAuthorizationService userAuthorizationService;
    @Mock
    private UserRepository userRepository;

    @Test
    void findUserVOByEmailTest() {
        UserVO userVO = Utils.getUserVO();
        when(userRepository.findUserVOByEmail(userVO.getEmail())).thenReturn(Optional.of(userVO));

        UserVO userVOByEmail = userAuthorizationService.findUserVOByEmail(userVO.getEmail());

        assertEquals(userVO, userVOByEmail);
    }

    @Test
    void findUserVOByEmailThrowsNotFoundExceptionIfNotFoundTest() {
        UserVO userVO = Utils.getUserVO();
        when(userRepository.findUserVOByEmail(userVO.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userAuthorizationService.findUserVOByEmail(userVO.getEmail()));
    }
}
