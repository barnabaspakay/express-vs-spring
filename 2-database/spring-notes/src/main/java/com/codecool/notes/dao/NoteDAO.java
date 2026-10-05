package com.codecool.notes.dao;

import com.codecool.notes.model.Note;

import java.util.List;

public interface NoteDAO {
    List<Note> getAll();

    Note getById(int id);

    Note create(String text);

    boolean remove(int id);
}
