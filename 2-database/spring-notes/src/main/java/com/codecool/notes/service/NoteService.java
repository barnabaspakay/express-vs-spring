package com.codecool.notes.service;

import com.codecool.notes.dao.NoteDAO;
import com.codecool.notes.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteDAO noteDAO;

    public NoteService(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public List<Note> getAll() {
        return noteDAO.getAll();
    }

    public Note getById(int id) {
        return noteDAO.getById(id);
    }

    public Note create(String text) {
        return noteDAO.create(text);
    }

    public boolean remove(int id) {
        return noteDAO.remove(id);
    }
}
