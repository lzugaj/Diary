package com.luv2code.diary.service;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;

import java.util.List;

public interface NoteService {

    Note save(final User user, final Note note);

    Note findById(final Long id);

    List<Note> findAll();

    List<Note> findAllForUser(final User user);

    Note update(final Note oldNote, final Note newNote);

    void delete(final User user, final Note note);

}
