package com.luv2code.diary.controller;

import com.luv2code.diary.domain.Note;
import com.luv2code.diary.domain.User;
import com.luv2code.diary.service.NoteService;
import com.luv2code.diary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/note")
public class NoteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;

    private final UserService userService;

    @Autowired
    public NoteController(final NoteService noteService,
                          final UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/show-form")
    public ModelAndView showNoteForm() {
        final ModelAndView modelAndView = new ModelAndView("/note/note-form");
        modelAndView.addObject("note", new Note());
        return modelAndView;
    }

    @PostMapping("/submit-form")
    public ModelAndView createNote(@ModelAttribute("note") Note note) {
        final User loggedInUser = userService.findByUsername("lzugaj");

        final Note newNote = noteService.save(loggedInUser, note);
        LOGGER.info("Successfully created new Note with id: ´{}´.", newNote.getId());

        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable Long id) {
        final Note searchedNote = noteService.findById(id);
        LOGGER.info("Successfully founded Note with id: ´{}´.", id);

        final ModelAndView modelAndView = new ModelAndView("/note/selected-note");
        modelAndView.addObject("selectedNote", searchedNote);
        return modelAndView;
    }

    @PostMapping("/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        final User loggedInUser = userService.findByUsername("lzugaj");

        final Note searchedNote = noteService.findById(id);
        LOGGER.info("Successfully founded Note with id: ´{}´.", id);

        noteService.delete(loggedInUser, searchedNote);
        LOGGER.info("Successfully deleted Note with id: ´{}´.", id);

        return new ModelAndView("redirect:/home");
    }
}
