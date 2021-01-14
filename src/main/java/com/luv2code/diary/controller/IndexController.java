package com.luv2code.diary.controller;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;
import com.luv2code.diary.service.NoteService;
import com.luv2code.diary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/home")
public class IndexController {

    public static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    private final NoteService noteService;

    private final UserService userService;

    @Autowired
    public IndexController(final NoteService noteService,
                          final UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView indexPage() {
        final User loggedInUser = userService.findByUsername("lzugaj");
        LOGGER.info("Successfully founded User with username: ´{}´.", "lzugaj");

        final List<Note> userNotes = noteService.findAllForUser(loggedInUser.getUsername());
        LOGGER.info("Successfully founded all Notes for User with username: ´{}´.", loggedInUser.getUsername());

        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("notes", userNotes);
        return modelAndView;
    }
}
