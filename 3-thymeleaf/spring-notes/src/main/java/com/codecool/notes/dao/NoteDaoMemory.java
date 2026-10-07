package com.codecool.notes.dao;

import com.codecool.notes.model.Note;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NoteDaoMemory implements NoteDAO {
    private final List<Note> notes = new ArrayList<>();
    private int nextId = 1;

    @Override
    public List<Note> getAll() {
        return notes;
    }

    @Override
    public Note getById(int id) {
        for (Note note : notes) {
            if (note.id() == id) {
                return note;
            }
        }
        return null;
    }

    @Override
    public Note create(String text) {
        Note note = new Note(nextId++, text, LocalDateTime.now());
        notes.add(note);
        return note;
    }

    @Override
    public boolean remove(int id) {
        return notes.removeIf(note -> note.id() == id);
    }
}
