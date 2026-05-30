package com.dypcoe.qsdta.service;

import com.dypcoe.qsdta.dao.AuthUserRepository;
import com.dypcoe.qsdta.exception.dao.AuthUserException;
import com.dypcoe.qsdta.model.AuthUser;
import com.dypcoe.qsdta.model.QsdtaUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser();
        authUser.setEmail("test@example.com");
        authUser.setFirstName("John");
        authUser.setLastName("Doe");
        authUser.setPicLink("http://image.url");
        authUser.setPassword("securepassword");
    }

    @Test
    void updateUser_Success() throws AuthUserException {
        when(userRepository.existsById(authUser.getUuid())).thenReturn(true);
        when(userRepository.save(authUser)).thenReturn(authUser);

        AuthUser updatedUser = userService.updateUser(authUser);

        assertNotNull(updatedUser);
        assertEquals(authUser.getEmail(), updatedUser.getEmail());
        verify(userRepository, times(1)).save(authUser);
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        when(userRepository.existsById(authUser.getUuid())).thenReturn(false);

        AuthUserException exception = assertThrows(AuthUserException.class, () -> userService.updateUser(authUser));

        assertTrue(exception.getMessage().contains("Invalid User Credential"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUser_Success() throws AuthUserException {
        when(userRepository.findById(authUser.getUuid())).thenReturn(Optional.of(authUser));

        QsdtaUser retrievedUser = userService.getUser(authUser.getUuid());

        assertNotNull(retrievedUser);
        assertEquals(authUser.getEmail(), retrievedUser.getEmail());
        verify(userRepository, times(1)).findById(authUser.getUuid());
    }

    @Test
    void getUser_UserNotFound_ThrowsException() {
        when(userRepository.findById(authUser.getUuid())).thenReturn(Optional.empty());

        AuthUserException exception = assertThrows(AuthUserException.class, () -> userService.getUser(authUser.getUuid()));

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(authUser.getUuid());
    }

    @Test
    void getUserPage_Success() throws AuthUserException {
        List<AuthUser> userList = List.of(authUser);
        Page<AuthUser> userPage = new PageImpl<>(userList);

        when(userRepository.findAllByOrderByFirstNameAsc(any(PageRequest.class))).thenReturn(userPage);

        List<QsdtaUser> result = userService.getUserPage(0, 10);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(authUser.getEmail(), result.get(0).getEmail());
        verify(userRepository, times(1)).findAllByOrderByFirstNameAsc(any(PageRequest.class));
    }

    @Test
    void getUserPage_NoUsers_ThrowsException() {
        Page<AuthUser> emptyPage = Page.empty();
        when(userRepository.findAllByOrderByFirstNameAsc(any(PageRequest.class))).thenReturn(emptyPage);

        AuthUserException exception = assertThrows(AuthUserException.class, () -> userService.getUserPage(0, 10));

        assertTrue(exception.getMessage().contains("No users found"));
        verify(userRepository, times(1)).findAllByOrderByFirstNameAsc(any(PageRequest.class));
    }

    @Test
    void deleteAuthUser_Success() throws AuthUserException {
        when(userRepository.existsById(authUser.getUuid())).thenReturn(true);
        doNothing().when(userRepository).deleteById(authUser.getUuid());

        assertDoesNotThrow(() -> userService.deleteAuthUser(authUser.getUuid()));

        verify(userRepository, times(1)).deleteById(authUser.getUuid());
    }

    @Test
    void deleteAuthUser_UserNotFound_ThrowsException() {
        when(userRepository.existsById(authUser.getUuid())).thenReturn(false);

        AuthUserException exception = assertThrows(AuthUserException.class, () -> userService.deleteAuthUser(authUser.getUuid()));

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, never()).deleteById(any());
    }
}
