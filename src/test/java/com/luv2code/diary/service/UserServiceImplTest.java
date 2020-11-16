package com.luv2code.diary.service;

import com.luv2code.diary.domain.User;
import com.luv2code.diary.repository.UserRepository;
import com.luv2code.diary.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User firstUser;

    private User secondUser;

    @BeforeEach
    public void setup() {
        firstUser = new User();
        firstUser.setId(1L);
        firstUser.setFirstName("Michael");
        firstUser.setLastName("Jordan");
        firstUser.setUsername("theGoat23");
        firstUser.setPassword("5Rings");
        firstUser.setEmail("michael.jordan23@gmail.com");
        firstUser.setCity("Chicago");
        firstUser.setCountry("USA");
        firstUser.setNotes(null);

        secondUser = new User();
        secondUser.setId(2L);
        secondUser.setFirstName("Kobe");
        secondUser.setLastName("Bryant");
        secondUser.setUsername("blackMamba24");
        secondUser.setPassword("mamba8");
        secondUser.setEmail("kobe.bryant248@gmail.com");
        secondUser.setCity("Los Angeles");
        secondUser.setCountry("USA");
        secondUser.setNotes(null);

        List<User> users = new ArrayList<>();
        users.add(firstUser);
        users.add(secondUser);

        Mockito.when(userRepository.save(firstUser)).thenReturn(firstUser);
        Mockito.when(userRepository.findById(secondUser.getId())).thenReturn(java.util.Optional.ofNullable(secondUser));
        Mockito.when(userRepository.findAll()).thenReturn(users);
    }

    @Test
    public void testSaveUser_ValidId_ShouldPass() {
        final User newUser = userService.save(firstUser);

        assertNotNull(newUser);
        assertEquals("1", newUser.getId().toString());
    }

    @Test
    public void testFindUser_ValidId_ShouldPass() {
        final User searchedUser = userService.findById(firstUser.getId());

        assertNotNull(searchedUser);
        assertEquals("2", searchedUser.getId().toString());
    }

    @Test
    public void testFindUser_NullId_ShouldThrowException() {
        when(userRepository.findById(firstUser.getId())).thenThrow(new NullPointerException());
        assertThrows(NullPointerException.class, () -> userService.findById(firstUser.getId()));
    }

    @Test
    public void testFindAll_ValidSize_ShouldPass() {
        final List<User> users = userService.findAll();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllEnabled_ValidSize_ShouldPass() {
        firstUser.setIsActive(false);
        secondUser.setIsActive(true);

        final List<User> users = userService.findAllEnabled();

        assertEquals(1, users.size());
    }

    @Test
    public void testFindAllDisabled_ValidSize_ShouldPass() {
        firstUser.setIsActive(true);
        secondUser.setIsActive(true);

        final List<User> users = userService.findAllDisabled();

        assertEquals(0, users.size());
    }

    @Test
    public void testChangeActiveState_ValidId_ShouldPass() {
        secondUser.setIsActive(true);

        final User user = userService.changeActiveState(secondUser.getId());

        assertEquals(false, user.getIsActive());
    }
}
