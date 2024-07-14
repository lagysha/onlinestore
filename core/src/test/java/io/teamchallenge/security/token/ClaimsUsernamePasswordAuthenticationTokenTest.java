package io.teamchallenge.security.token;

import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClaimsUsernamePasswordAuthenticationTokenTest {
    @InjectMocks
    private ClaimsUsernamePasswordAuthenticationToken authenticationToken;
    @Mock
    private DefaultClaims claims;

    @Test
    void getUserIdTest() {
        Long userId = 1L;
        when(claims.get("id", Long.class)).thenReturn(userId);

        Long actual = authenticationToken.getUserId();

        assertEquals(userId, actual);
        verify(claims).get("id", Long.class);
    }
}
