package com.luv2code.diary.service;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;

import java.util.List;

public interface SortService {

    List<Note> sortByCreationDate(final List<Note> notes);

    List<User> sortByUsername(final List<User> users);

}
