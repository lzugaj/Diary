package com.luv2code.diary.service.impl;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.exception.UserNotActiveException;
import com.luv2code.diary.repository.NoteRepository;
import com.luv2code.diary.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.luv2code.diary.domain.enums.UserStatus.ACTIVE;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(final NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note save(final User user, final Note note) {
        if (user.getStatus().equals(ACTIVE)) {
            setupVariablesCreate(user, note);
            LOGGER.info("Successfully setup up variables for User with username: ´{}´.", user.getUsername());

            final Note newNote = noteRepository.save(note);
            LOGGER.info("Creating Note with id: ´{}´.", note.getId());
            return newNote;
        } else {
            LOGGER.error("Cannot create Note for not active User with username: ´{}´.", user.getUsername());
            throw new UserNotActiveException("User", "username", user.getUsername());
        }
    }

    private void setupVariablesCreate(final User user, final Note newNote) {
        LOGGER.info("Setting up variables for User with username: ´{}´.", user.getUsername());
        newNote.setCreateDate(LocalDateTime.now());
        newNote.setUser(user);
        user.setNumberOfNotes(user.getNumberOfNotes() + 1);
        user.setNotes(Collections.singletonList(newNote));
    }

    @Override
    public Note findById(final Long id) {
        final Optional<Note> searchedNote = noteRepository.findById(id);
        if (searchedNote.isPresent()) {
            LOGGER.info("Searching Note with id: ´{}´.", id);
            return searchedNote.get();
        } else {
            LOGGER.error("Note not founded with id: ´{}´.", id);
            throw new EntityNotFoundException("Note", "id", String.valueOf(id));
        }
    }

    @Override
    public List<Note> findAll() {
        final List<Note> notes = noteRepository.findAll();
        LOGGER.info("Searching all Notes.");

        Collections.sort(notes);
        LOGGER.info("Sorting all Notes by event date.");
        return notes;
    }

    @Override
    public List<Note> findAllForUser(final User user) {
        final List<Note> notes = findAll();
        LOGGER.info("Searching all Notes for User with id: ´{}´.", user.getId());
        return notes.stream()
                .filter(sortedUser -> sortedUser.getUser().getUsername().equals(user.getUsername()))
                .collect(Collectors.toList());
    }

    @Override
    public Note update(final Note oldNote, final Note newNote) {
        setupVariablesUpdate(oldNote, newNote);
        LOGGER.info("Successfully setup variables for Note with id: ´{}´.", oldNote.getId());

        noteRepository.save(oldNote);
        LOGGER.info("Updating Note with id: ´{}´.", oldNote.getId());
        return oldNote;
    }

    private void setupVariablesUpdate(final Note oldNote, final Note newNote) {
        LOGGER.info("Setting up variables for Note with id: ´{}´.", oldNote.getId());
        oldNote.setTitle(newNote.getTitle());
        oldNote.setDescription(newNote.getDescription());
        oldNote.setLocation(newNote.getLocation());
        oldNote.setEventDate(newNote.getEventDate());
    }

    @Override
    public void delete(final User user, final Note note) {
        user.setNumberOfNotes(user.getNumberOfNotes() - 1);

        LOGGER.info("Deleting Note with id: ´{}´.", note.getId());
        noteRepository.delete(note);
    }
}
