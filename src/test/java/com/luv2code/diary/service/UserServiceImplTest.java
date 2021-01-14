package com.luv2code.diary.service;

import com.luv2code.diary.domain.User;
import com.luv2code.diary.exception.UsernameAlreadyExistException;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.exception.UserNotActiveException;
import com.luv2code.diary.repository.UserRepository;
import com.luv2code.diary.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.luv2code.diary.domain.enums.UserStatus.ACTIVE;
import static com.luv2code.diary.domain.enums.UserStatus.INACTIVE;

@SpringBootTest
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User firstUser;

    private User secondUser;

    private User thirdUser;

    @BeforeEach
    public void setup() {
        firstUser = new User();
        firstUser.setId(1L);
        firstUser.setFirstName("Michael");
        firstUser.setLastName("Jordan");
        firstUser.setUsername("blackMamba24");
        firstUser.setPassword("5Rings");
        firstUser.setEmail("michael.jordan23@gmail.com");
        firstUser.setStatus(ACTIVE);
        firstUser.setNumberOfNotes(0);
        firstUser.setNotes(null);

        secondUser = new User();
        secondUser.setId(2L);
        secondUser.setFirstName("Kobe");
        secondUser.setLastName("Bryant");
        secondUser.setUsername("blackMamba24");
        secondUser.setPassword("mamba8");
        secondUser.setEmail("kobe.bryant248@gmail.com");
        secondUser.setStatus(INACTIVE);
        secondUser.setNumberOfNotes(0);
        secondUser.setNotes(null);

        thirdUser = new User();
        thirdUser.setId(3L);
        thirdUser.setFirstName("Lebron");
        thirdUser.setLastName("James");
        thirdUser.setUsername("theKing23");
        thirdUser.setPassword("chosenOne");
        thirdUser.setEmail("lebron23@gmail.com");
        thirdUser.setStatus(ACTIVE);
        thirdUser.setNumberOfNotes(0);
        thirdUser.setNotes(null);

        final List<User> users = new ArrayList<>();
        users.add(firstUser);
        users.add(secondUser);

        Mockito.when(userRepository.save(thirdUser)).thenReturn(thirdUser);
        Mockito.when(userRepository.findById(secondUser.getId())).thenReturn(java.util.Optional.ofNullable(secondUser));
        Mockito.when(userRepository.findByUsername(firstUser.getUsername())).thenReturn(java.util.Optional.ofNullable(firstUser));
        Mockito.when(userRepository.findAll()).thenReturn(users);
    }

    @Test
    public void should_Save_When_Username_Not_In_Use() {
        final User newUser = userService.save(thirdUser);

        Assertions.assertNotNull(newUser);
        Assertions.assertEquals("3", String.valueOf(newUser.getId()));
    }

    @Test
    public void should_Throw_Exception_When_Username_Already_Exists() {
        Mockito.when(userRepository.save(firstUser))
                .thenThrow(new UsernameAlreadyExistException(
                        "User",
                        "username",
                        secondUser.getUsername())
                );

        final Exception exception = Assertions.assertThrows(
                UsernameAlreadyExistException.class,
                () -> userService.save(firstUser)
        );

        final String expectedMessage = "Entity 'User' with 'username' value 'blackMamba24' already exists.";
        final String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void should_Find_When_Id_Is_Valid() {
        final User searchedUser = userService.findById(secondUser.getId());

        Assertions.assertNotNull(searchedUser);
        Assertions.assertEquals("2", String.valueOf(searchedUser.getId()));
    }

    @Test
    public void should_Throw_Exception_When_Id_Is_Not_Valid() {
        Mockito.when(userRepository.findById(thirdUser.getId()))
                .thenThrow(new EntityNotFoundException(
                        "User",
                        "id",
                        String.valueOf(thirdUser.getId()))
                );

        final Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(thirdUser.getId())
        );

        final String expectedMessage = "Entity 'User' with 'id' value '3' not founded.";
        final String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void should_Find_User_When_Username_Is_Valid() {
        final User searchedUser = userService.findByUsername(firstUser.getUsername());

        Assertions.assertNotNull(searchedUser);
        Assertions.assertEquals("blackMamba24", searchedUser.getUsername());
    }

    @Test
    public void should_Throw_Exception_When_Username_Is_Not_Valid() {
        Mockito.when(userRepository.findByUsername(thirdUser.getUsername()))
                .thenThrow(new EntityNotFoundException(
                        "User",
                        "username",
                        thirdUser.getUsername())
                );

        final Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.findByUsername(thirdUser.getUsername())
        );

        final String expectedMessage = "Entity 'User' with 'username' value 'theKing23' not founded.";
        final String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void should_Find_All() {
        final List<User> users = userService.findAll();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(2, users.size());
    }

    @Test
    public void should_Change_Status_To_False() {
        secondUser.setStatus(ACTIVE);
        final User user = userService.changeStatus(secondUser);

        Assertions.assertEquals("2", String.valueOf(user.getId()));
        Assertions.assertEquals("INACTIVE", String.valueOf(user.getStatus()));
    }

    @Test
    public void should_Change_Status_To_True() {
        secondUser.setStatus(INACTIVE);
        final User user = userService.changeStatus(secondUser);

        Assertions.assertEquals("2", String.valueOf(user.getId()));
        Assertions.assertEquals("ACTIVE", String.valueOf(user.getStatus()));
    }

    @Test
    public void should_Update_When_Is_Active() {
        final User updatedUser = userService.update(thirdUser, secondUser);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("3", String.valueOf(updatedUser.getId()));
        Assertions.assertEquals("Kobe", updatedUser.getFirstName());
        Assertions.assertEquals("Bryant", updatedUser.getLastName());
        Assertions.assertEquals("blackMamba24", updatedUser.getUsername());
        Assertions.assertEquals("kobe.bryant248@gmail.com", updatedUser.getEmail());
    }

    @Test
    public void should_Throw_Exception_When_Status_Is_Inactive() {
        Mockito.when(userRepository.save(secondUser))
                .thenThrow(new UserNotActiveException(
                        "User",
                        "id",
                        String.valueOf(secondUser.getId()))
                );

        final Exception exception = Assertions.assertThrows(
                UserNotActiveException.class,
                () -> userService.update(secondUser, firstUser)
        );

        final String expectedMessage = "Entity 'User' with 'id' value '2' is not active.";
        final String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void should_Delete_If_Valid() {
        userService.delete(firstUser);

        Mockito.verify(userRepository, Mockito.times(1)).delete(firstUser);
    }
}
