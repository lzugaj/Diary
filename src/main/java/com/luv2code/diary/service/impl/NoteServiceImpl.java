package com.luv2code.diary.service.impl;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.repository.NoteRepository;
import com.luv2code.diary.service.NoteService;
import com.luv2code.diary.service.SortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    private final SortService sortService;

    @Autowired
    public NoteServiceImpl(final NoteRepository noteRepository,
                           final SortService sortService) {
        this.noteRepository = noteRepository;
        this.sortService = sortService;
    }

    @Override
    public Note save(final User user, final Note note) {
        setupVariablesCreate(user, note);
        LOGGER.info("Successfully setup up variables for User with username: ´{}´.", user.getUsername());

        final Note newNote = noteRepository.save(note);
        LOGGER.info("Saving new Note with id: ´{}´.", note.getId());
        return newNote;
    }

    private void setupVariablesCreate(final User user, final Note newNote) {
        LOGGER.info("Setting up variables for User with username: ´{}´.", user.getUsername());
        newNote.setCreationDate(LocalDateTime.now());
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
        return notes;
    }

    @Override
    public List<Note> findAllForUser(final String username) {
        final List<Note> notes = findAll();
        LOGGER.info("Successfully founded all Notes.");

        final List<Note> sortedNotes = sortService.sortByCreationDate(notes);
        LOGGER.info("Successfully sorted all Notes by created date for User with username: ´{}´", username);

        LOGGER.info("Searching all Notes for User with username: ´{}´.", username);
        return sortedNotes.stream()
                .filter(user -> user.getUser().getUsername().equals(username))
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
