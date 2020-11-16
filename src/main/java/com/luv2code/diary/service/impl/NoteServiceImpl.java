package com.luv2code.diary.service.impl;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.repository.NoteRepository;
import com.luv2code.diary.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(final NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // TODO: lzugaj dodati note za tog usera
    @Override
    public Note save(final Note note) {
        note.setCreationDate(LocalDate.now());

        final Note newNote = noteRepository.save(note);
        LOGGER.info("Saving new Note with id: ´{}´.", note.getId());
        return newNote;
    }

    @Override
    public Note findById(final Long id) {
        final Note searchedNote = noteRepository.findById(id).orElse(null);
        LOGGER.info("Searching Note with id: ´{}´.", id);
        return searchedNote;
    }

    // TODO: Sort by date
    @Override
    public List<Note> findAll() {
        final List<Note> notes = noteRepository.findAll();
        LOGGER.info("Searching all Notes.");
        return notes;
    }

//    @Override
//    public List<Note> findAllForUser(String username) {
//        final List<Note> notes = findAll();
//        LOGGER.info("Searching all Notes for User with username: ´{}´", username);
//    }

    // TODO: lzugaj
    @Override
    public Note update(Note oldNote, Note newNote) {
        return null;
    }

    @Override
    public void delete(final Note note) {
        noteRepository.delete(note);
        LOGGER.info("Deleting Note with id: ´{}´", note.getId());
    }
}
