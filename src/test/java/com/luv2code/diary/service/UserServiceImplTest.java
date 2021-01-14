package com.luv2code.diary.service;

import com.luv2code.diary.domain.User;
import com.luv2code.diary.exception.EntityAlreadyExistException;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.repository.UserRepository;
import com.luv2code.diary.service.impl.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User firstUser;

    private User secondUser;

    private User thirdUser;

    @Before
    public void setup() {
        firstUser = new User();
        firstUser.setId(1L);
        firstUser.setFirstName("Michael");
        firstUser.setLastName("Jordan");
        firstUser.setUsername("theGoat");
        firstUser.setPassword("5Rings");
        firstUser.setEmail("michael.jordan23@gmail.com");
        firstUser.setCity("Chicago");
        firstUser.setCountry("USA");
        firstUser.setIsActive(true);
        firstUser.setNumberOfNotes(0);
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
        secondUser.setIsActive(true);
        secondUser.setNumberOfNotes(0);
        secondUser.setNotes(null);

        thirdUser = new User();
        thirdUser.setId(3L);
        thirdUser.setFirstName("Lebron");
        thirdUser.setLastName("James");
        thirdUser.setUsername("theKing23");
        thirdUser.setPassword("chosenOne");
        thirdUser.setEmail("lebron23@gmail.com");
        thirdUser.setCity("Cleveland");
        thirdUser.setCountry("USA");
        thirdUser.setIsActive(false);
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
    public void should_Save_User() {
        final User newUser = userService.save(thirdUser);

        Assert.assertNotNull(newUser);
        Assert.assertEquals("3", String.valueOf(newUser.getId()));
    }

    @Test
    public void should_Return_EntityAlreadyExist_Exception() {
        Mockito.when(userRepository.save(secondUser))
                .thenThrow(new EntityAlreadyExistException("User", "username", secondUser.getUsername()));

        Assert.assertThrows(EntityAlreadyExistException.class, () -> userService.save(secondUser));
    }

    @Test
    public void should_Return_User_When_Id_Is_Valid() {
        final User searchedUser = userService.findById(secondUser.getId());

        Assert.assertNotNull(searchedUser);
        Assert.assertEquals("2", String.valueOf(searchedUser.getId()));
    }

    @Test
    public void should_Return_EntityNotFound_Exception_When_Id_Is_Not_Valid() {
        Mockito.when(userRepository.findById(thirdUser.getId()))
                .thenThrow(new EntityNotFoundException("User", "id", String.valueOf(thirdUser.getId())));

        Assert.assertThrows(EntityNotFoundException.class, () -> userService.findById(firstUser.getId()));
    }

    @Test
    public void should_Return_User_When_Username_Is_Valid() {
        final User searchedUser = userService.findByUsername(firstUser.getUsername());

        Assert.assertNotNull(searchedUser);
        Assert.assertEquals("theGoat", searchedUser.getUsername());
    }

    @Test
    public void should_Return_EntityNotFound_Exception_When_Username_Is_Not_Valid() {
        Mockito.when(userRepository.findByUsername(thirdUser.getUsername()))
                .thenThrow(new EntityNotFoundException("User", "username", thirdUser.getUsername()));

        Assert.assertThrows(EntityNotFoundException.class, () -> userService.findByUsername(thirdUser.getUsername()));
    }

    @Test
    public void should_Return_All_Users() {
        final List<User> users = userService.findAll();

        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
    }

    @Test
    public void should_Return_Active_State_To_False() {
        secondUser.setIsActive(true);
        final User user = userService.changeStatus(secondUser);

        Assert.assertEquals(false, user.getIsActive());
    }

    @Test
    public void should_Return_Active_State_To_True() {
        secondUser.setIsActive(false);
        final User user = userService.changeStatus(secondUser);

        Assert.assertEquals(true, user.getIsActive());
    }
}
