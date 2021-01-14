package com.luv2code.diary.service;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.exception.UserNotActiveException;
import com.luv2code.diary.repository.NoteRepository;
import com.luv2code.diary.service.impl.NoteServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.luv2code.diary.domain.enums.UserStatus.ACTIVE;
import static com.luv2code.diary.domain.enums.UserStatus.INACTIVE;

@SpringBootTest
public class NoteServiceImplTest {

    @InjectMocks
    private NoteServiceImpl noteService;

    @Mock
    private NoteRepository noteRepository;

    private User firstUser;
    private User secondUser;
    private User thirdUser;

    private Note firstNote;
    private Note secondNote;
    private Note thirdNote;

    @BeforeEach
    public void setup() throws ParseException {
        firstUser = new User();
        firstUser.setId(1L);
        firstUser.setFirstName("Michael");
        firstUser.setLastName("Jordan");
        firstUser.setUsername("mj23");
        firstUser.setPassword("TheGoat23");
        firstUser.setEmail("michael.jordan23@gmail.com");
        firstUser.setNumberOfNotes(0);
        firstUser.setStatus(ACTIVE);

        secondUser = new User();
        secondUser.setId(2L);
        secondUser.setFirstName("Lebron");
        secondUser.setLastName("James");
        secondUser.setUsername("lebron23");
        secondUser.setPassword("TheChosenOne");
        secondUser.setEmail("lebron.james23@gmail.com");
        secondUser.setNumberOfNotes(0);
        secondUser.setStatus(ACTIVE);

        thirdUser = new User();
        thirdUser.setId(3L);
        thirdUser.setFirstName("Kobe");
        thirdUser.setLastName("Bryant");
        thirdUser.setUsername("BlackMamba824");
        thirdUser.setPassword("kobe");
        thirdUser.setEmail("kobe.bryant@gmail.com");
        thirdUser.setNumberOfNotes(0);
        thirdUser.setStatus(INACTIVE);

        firstNote = new Note();
        firstNote.setId(1L);
        firstNote.setTitle("Watching TV");
        firstNote.setDescription("Today I was watching Titanic with my family.");
        firstNote.setLocation("Zagreb");
        firstNote.setEventDate(parseDate("2020-11-22"));
        firstNote.setCreateDate(LocalDateTime.now());
        firstNote.setUser(firstUser);

        secondNote = new Note();
        secondNote.setId(2L);
        secondNote.setTitle("Developing new app");
        secondNote.setDescription("I am working on app for diary.");
        secondNote.setLocation("Barcelona");
        secondNote.setEventDate(parseDate("2020-11-25"));
        secondNote.setCreateDate(LocalDateTime.now());
        secondNote.setUser(secondUser);

        thirdNote = new Note();
        thirdNote.setId(3L);
        thirdNote.setTitle("Can't wait Christmas");
        thirdNote.setDescription("It is just 1 month till Christmas.");
        thirdNote.setLocation("North Pole");
        thirdNote.setEventDate(parseDate("2020-11-25"));
        thirdNote.setCreateDate(LocalDateTime.now());
        thirdNote.setUser(firstUser);

        firstUser.setNotes(Collections.singletonList(firstNote));
        firstUser.setNotes(Collections.singletonList(thirdNote));
        secondUser.setNotes(Collections.singletonList(secondNote));

        List<Note> notes = new ArrayList<>();
        notes.add(firstNote);
        notes.add(secondNote);
        notes.add(thirdNote);

        firstUser.setNumberOfNotes(firstUser.getNumberOfNotes() + 1);
        firstUser.setNumberOfNotes(firstUser.getNumberOfNotes() + 1);
        secondUser.setNumberOfNotes(secondUser.getNumberOfNotes() + 1);

        Mockito.when(noteRepository.save(firstNote)).thenReturn(firstNote);
        Mockito.when(noteRepository.findById(secondNote.getId())).thenReturn(java.util.Optional.ofNullable(secondNote));
        Mockito.when(noteRepository.findAll()).thenReturn(notes);
    }

    @Test
    public void should_Save_When_User_Is_Active() {
        final Note newNote = noteService.save(firstUser, firstNote);

        Assertions.assertNotNull(newNote);
        Assertions.assertEquals("1", String.valueOf(newNote.getId()));
        Assertions.assertEquals("mj23", newNote.getUser().getUsername());
    }

    @Test
    public void should_Throw_Exception_When_User_Is_Not_Active() {
        Mockito.when(noteRepository.save(thirdNote))
                .thenThrow(new UserNotActiveException(
                        "User",
                        "username",
                        thirdUser.getUsername()
                ));

        final Exception exception = Assertions.assertThrows(
                UserNotActiveException.class,
                () -> noteService.save(thirdUser, thirdNote)
        );

        final String expectedMessage = "Entity 'User' with 'username' value 'BlackMamba824' is not active.";
        final String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void should_Find_When_Id_Is_Valid() {
        final Note searchedNote = noteService.findById(secondNote.getId());

        Assertions.assertNotNull(searchedNote);
        Assertions.assertEquals("2", String.valueOf(searchedNote.getId()));
        Assertions.assertEquals("lebron23", searchedNote.getUser().getUsername());
    }

    @Test
    public void should_Throw_Exception_When_Id_Is_Not_Valid() {
        Mockito.when(noteRepository.findById(thirdNote.getId()))
                .thenThrow(new EntityNotFoundException(
                        "Note",
                        "id",
                        String.valueOf(thirdNote.getId()))
                );

        final Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> noteService.findById(thirdNote.getId())
        );

        final String expectedMessage = "Entity 'Note' with 'id' value '3' not founded.";
        final String actualMessage = exception.getMessage();

        Assertions.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void should_Find_All() {
        final List<Note> searchedNotes = noteService.findAll();

        Assertions.assertNotNull(searchedNotes);
        Assertions.assertEquals(3, searchedNotes.size());
    }

    @Test
    public void should_Find_All_For_User() {
        final List<Note> searchedNotes = noteService.findAllForUser(secondUser);

        Assertions.assertNotNull(searchedNotes);
        Assertions.assertEquals(1, searchedNotes.size());
    }

    @Test
    public void should_Update_If_Valid() {
        final Note updatedNote = noteService.update(firstNote, secondNote);

        Assertions.assertNotNull(updatedNote);
        Assertions.assertEquals("1", String.valueOf(updatedNote.getId()));
        Assertions.assertEquals("Developing new app", updatedNote.getTitle());
        Assertions.assertEquals("I am working on app for diary.", updatedNote.getDescription());
        Assertions.assertEquals("Barcelona", updatedNote.getLocation());
    }

    @Test
    public void should_Delete_If_Valid() {
        noteService.delete(firstUser, thirdNote);

        Mockito.verify(noteRepository, Mockito.times(1)).delete(thirdNote);
        Assertions.assertEquals("1", String.valueOf(firstUser.getNumberOfNotes()));
    }

    private Date parseDate(final String dateString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    }
}
