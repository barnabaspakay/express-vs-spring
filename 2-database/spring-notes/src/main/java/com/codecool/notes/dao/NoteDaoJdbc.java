package com.codecool.notes.dao;

import com.codecool.notes.model.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDaoJdbc implements NoteDAO {
    private final DatabaseConnection databaseConnection;

    public NoteDaoJdbc(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public List<Note> getAll() {
        String sql = "SELECT id, text FROM note ORDER BY id";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            List<Note> notes = new ArrayList<>();
            while (rs.next()) {
                notes.add(new Note(rs.getInt("id"), rs.getString("text")));
            }
            return notes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Note getById(int id) {
        String sql = "SELECT id, text FROM note WHERE id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return new Note(rs.getInt("id"), rs.getString("text"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Note create(String text) {
        String sql = "INSERT INTO note (text) VALUES (?) RETURNING id, text";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, text);
            ResultSet rs = st.executeQuery();
            rs.next();
            return new Note(rs.getInt("id"), rs.getString("text"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM note WHERE id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
