package com.luv2code.diary.service.impl;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;
import com.luv2code.diary.service.SortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SortServiceImpl implements SortService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SortServiceImpl.class);

    @Override
    public List<Note> sortByCreationDate(final List<Note> notes) {
        LOGGER.info("Sorting all Notes by creation date.");
        return notes.stream()
                .sorted(Comparator.comparing(Note::getEventDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> sortByUsername(final List<User> users) {
        LOGGER.info("Sorting all Users by username");
        return users.stream()
                .sorted(Comparator.comparing(User::getUsername))
                .collect(Collectors.toList());
    }
}
