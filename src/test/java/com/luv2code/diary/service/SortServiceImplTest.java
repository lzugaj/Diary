package com.luv2code.diary.service;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;
import com.luv2code.diary.service.impl.SortServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(MockitoJUnitRunner.Silent.class)
public class SortServiceImplTest {

    @InjectMocks
    private SortServiceImpl sortService;

    private List<Note> unsortedNotes;

    private List<User> unsortedUsers;

    @Before
    public void setup() throws ParseException {
        Note firstNote = new Note();
        firstNote.setId(1L);
        firstNote.setTitle("Title 1");
        firstNote.setDescription("Description 1");
        firstNote.setLocation("Zagreb");
        firstNote.setEventDate(parseDate("2020-04-22"));
        firstNote.setCreationDate(LocalDateTime.now());

        Note secondNote = new Note();
        secondNote.setId(2L);
        secondNote.setTitle("Title 2");
        secondNote.setDescription("Description 2");
        secondNote.setLocation("Zadar");
        secondNote.setEventDate(parseDate("2020-05-13"));
        secondNote.setCreationDate(LocalDateTime.now());

        Note thirdNote = new Note();
        thirdNote.setId(3L);
        thirdNote.setTitle("Title 3");
        thirdNote.setDescription("Description 3");
        thirdNote.setLocation("Murter");
        thirdNote.setEventDate(parseDate("2020-05-11"));
        thirdNote.setCreationDate(LocalDateTime.now());

        unsortedNotes = new ArrayList<>();
        unsortedNotes.add(firstNote);
        unsortedNotes.add(secondNote);
        unsortedNotes.add(thirdNote);

        List<Note> sortedNotes = new ArrayList<>();
        sortedNotes.add(secondNote);
        sortedNotes.add(thirdNote);
        sortedNotes.add(firstNote);

        User firstUser = new User();
        firstUser.setId(1L);
        firstUser.setFirstName("Michael");
        firstUser.setLastName("Jordan");
        firstUser.setUsername("theGoat23");
        firstUser.setPassword("5Rings");
        firstUser.setEmail("michael.jordan23@gmail.com");
        firstUser.setCity("Chicago");
        firstUser.setCountry("USA");

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setFirstName("Kobe");
        secondUser.setLastName("Bryant");
        secondUser.setUsername("blackMamba24");
        secondUser.setPassword("mamba8");
        secondUser.setEmail("kobe.bryant248@gmail.com");
        secondUser.setCity("Los Angeles");
        secondUser.setCountry("USA");

        User thirdUser = new User();
        thirdUser.setId(3L);
        thirdUser.setFirstName("Lebron");
        thirdUser.setLastName("James");
        thirdUser.setUsername("theKing23");
        thirdUser.setPassword("chosenOne");
        thirdUser.setEmail("lebron23@gmail.com");
        thirdUser.setCity("Cleveland");
        thirdUser.setCountry("USA");

        unsortedUsers = new ArrayList<>();
        unsortedUsers.add(firstUser);
        unsortedUsers.add(secondUser);
        unsortedUsers.add(thirdUser);

        List<User> sortedUsers = new ArrayList<>();
        sortedUsers.add(secondUser);
        sortedUsers.add(firstUser);
        sortedUsers.add(thirdUser);
    }

    @Test
    public void testSortByCreationDate() {
        final List<Note> notes = sortService.sortByCreationDate(unsortedNotes);

        Assert.assertNotNull(notes);
        Assert.assertEquals(3, notes.size());
    }

    @Test
    public void testSortByUsername() {
        final List<User> users = sortService.sortByUsername(unsortedUsers);

        Assert.assertNotNull(users);
        Assert.assertEquals(3, users.size());
    }

    private Date parseDate(final String dateString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    }
}
