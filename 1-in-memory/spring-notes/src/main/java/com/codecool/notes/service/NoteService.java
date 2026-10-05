package com.codecool.notes.service;

import com.codecool.notes.model.Note;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    private final List<Note> notes = new ArrayList<>();
    private int nextId = 1;

    public List<Note> getAll() {
        return notes;
    }

    public Note getById(int id) {
        for (Note note : notes) {
            if (note.id() == id) {
                return note;
            }
        }
        return null;
    }

    public Note create(String text) {
        Note note = new Note(nextId++, text);
        notes.add(note);
        return note;
    }

    public boolean remove(int id) {
        return notes.removeIf(note -> note.id() == id);
    }
}
