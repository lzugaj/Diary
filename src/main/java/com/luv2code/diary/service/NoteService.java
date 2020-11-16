package com.luv2code.diary.service;

import com.luv2code.diary.domain.Note;

import java.util.List;

public interface NoteService {

    Note save(final Note note);

    Note findById(final Long id);

    List<Note> findAll();

    List<Note> findAllForUser(final String username);

    Note update(final Note oldNote, final Note newNote);

    void delete(final Note note);

}
