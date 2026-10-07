package com.codecool.notes.model;

import java.time.LocalDateTime;

public record Note(int id, String text, LocalDateTime created) {
}
