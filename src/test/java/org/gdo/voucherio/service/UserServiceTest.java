package org.gdo.voucherio.service;

import org.gdo.voucherio.voucher.model.User;
import org.gdo.voucherio.voucher.repository.UserRepository;
import org.gdo.voucherio.voucher.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testLoadUserByName() throws Exception {

        // test data
        final User expectedUser = new User("Jos", true, "2025-03-04", "password");
        final String username = "Jos";

        // behaviour
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        // test
        assertEquals(expectedUser, userService.loadUserByUsername(username));
    }

    @Test
    public void testLoadUserByNameNotFound() throws Exception {

        // test data
        final String username = "Jos";

        // behaviour
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // test
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }
}
