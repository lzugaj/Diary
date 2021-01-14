package com.luv2code.diary.service;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.repository.NoteRepository;
import com.luv2code.diary.service.impl.NoteServiceImpl;
import org.junit.Assert;
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

@SpringBootTest
public class NoteServiceImplTest {

    @InjectMocks
    private NoteServiceImpl noteService;

    @Mock
    private NoteRepository noteRepository;

    private User firstUser;
    private User secondUser;

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
        firstUser.setCity("Chicago");
        firstUser.setCountry("USA");
        firstUser.setNumberOfNotes(0);
        firstUser.setIsActive(true);

        secondUser = new User();
        secondUser.setId(2L);
        secondUser.setFirstName("Lebron");
        secondUser.setLastName("James");
        secondUser.setUsername("lebron23");
        secondUser.setPassword("TheChosenOne");
        secondUser.setEmail("lebron.james23@gmail.com");
        secondUser.setCity("Cleveland");
        secondUser.setCountry("USA");
        secondUser.setNumberOfNotes(0);
        secondUser.setIsActive(true);

        firstNote = new Note();
        firstNote.setId(1L);
        firstNote.setTitle("Watching TV");
        firstNote.setDescription("Today I was watching Titanic with my family.");
        firstNote.setLocation("Zagreb");
        firstNote.setEventDate(parseDate("2020-11-22"));
        firstNote.setCreationDate(LocalDateTime.now());
        firstNote.setUser(firstUser);

        secondNote = new Note();
        secondNote.setId(2L);
        secondNote.setTitle("Developing new app");
        secondNote.setDescription("I am working on app for diary.");
        secondNote.setLocation("Barcelona");
        secondNote.setEventDate(parseDate("2020-11-25"));
        secondNote.setCreationDate(LocalDateTime.now());
        secondNote.setUser(secondUser);

        thirdNote = new Note();
        thirdNote.setId(3L);
        thirdNote.setTitle("Can't wait Christmas");
        thirdNote.setDescription("It is just 1 month till Christmas.");
        thirdNote.setLocation("North Pole");
        thirdNote.setEventDate(parseDate("2020-11-25"));
        thirdNote.setCreationDate(LocalDateTime.now());
        thirdNote.setUser(firstUser);

        firstUser.setNotes(Collections.singletonList(firstNote));
        firstUser.setNotes(Collections.singletonList(thirdNote));
        secondUser.setNotes(Collections.singletonList(secondNote));

        List<Note> notes = new ArrayList<>();
        notes.add(firstNote);
        notes.add(secondNote);
        notes.add(thirdNote);

        Mockito.when(noteRepository.save(firstNote)).thenReturn(firstNote);
        Mockito.when(noteRepository.findById(secondNote.getId())).thenReturn(java.util.Optional.ofNullable(secondNote));
        Mockito.when(noteRepository.findAll()).thenReturn(notes);
    }

    @Test
    public void should_Save_Note() {
        final Note newNote = noteService.save(firstUser, firstNote);

        Assertions.assertNotNull(newNote);
        Assertions.assertEquals("1", String.valueOf(newNote.getId()));
        Assertions.assertEquals("mj23", newNote.getUser().getUsername());
    }

    @Test
    public void should_Return_Note_When_Id_Is_Valid() {
        final Note searchedNote = noteService.findById(secondNote.getId());

        Assertions.assertNotNull(searchedNote);
        Assertions.assertEquals("2", String.valueOf(searchedNote.getId()));
        Assertions.assertEquals("lebron23", searchedNote.getUser().getUsername());
    }

    @Test
    public void should_Return_EntityNotFound_Exception_When_Id_Is_Not_Valid() {
        Mockito.when(noteRepository.findById(thirdNote.getId()))
                .thenThrow(new EntityNotFoundException(
                        "Note",
                        "id",
                        String.valueOf(thirdNote.getId())));

        Assert.assertThrows(EntityNotFoundException.class, () -> noteService.findById(thirdNote.getId()));
    }

    @Test
    public void should_Return_All_Notes() {
        final List<Note> searchedNotes = noteService.findAll();

        Assertions.assertNotNull(searchedNotes);
        Assertions.assertEquals(3, searchedNotes.size());
    }

    @Test
    public void should_Return_All_For_User() {
        final List<Note> searchedNotes = noteService.findAllForUser(secondUser.getUsername());

        Assertions.assertNotNull(searchedNotes);
        Assertions.assertEquals(1, searchedNotes.size());
    }

//    @Test
//    public void should_Delete_Note() {
//        noteService.delete(thirdNote);
//
//        Mockito.verify(noteRepository, Mockito.times(1)).delete(thirdNote);
//        Assertions.assertEquals("3", String.valueOf(thirdNote.getId()));
//    }

    private Date parseDate(final String dateString) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
    }
}
