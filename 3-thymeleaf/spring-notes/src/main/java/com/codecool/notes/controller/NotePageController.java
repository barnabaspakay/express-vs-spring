package com.codecool.notes.controller;

import com.codecool.notes.model.Note;
import com.codecool.notes.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class NotePageController {
    private final NoteService noteService;

    public NotePageController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/notes";
    }

    @GetMapping("/notes")
    public String list(Model model) {
        model.addAttribute("notes", noteService.getAll());
        return "notes";
    }

    @GetMapping("/notes/{id}")
    public String detail(@PathVariable int id, Model model) {
        Note note = noteService.getById(id);
        if (note == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("note", note);
        return "note";
    }

    @PostMapping("/notes")
    public String create(@RequestParam String text) {
        Note note = noteService.create(text);
        return "redirect:/notes/" + note.id();
    }

    @PostMapping("/notes/{id}/delete")
    public String delete(@PathVariable int id) {
        noteService.remove(id);
        return "redirect:/notes";
    }
}
