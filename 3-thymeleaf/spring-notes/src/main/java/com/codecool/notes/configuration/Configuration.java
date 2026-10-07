package com.codecool.notes.configuration;

import com.codecool.notes.dao.DatabaseConnection;
import com.codecool.notes.dao.NoteDAO;
import com.codecool.notes.dao.NoteDaoMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class Configuration {

    @Value("${notes.database.url}")
    private String databaseUrl;

    @Value("${notes.database.username}")
    private String username;

    @Value("${notes.database.password}")
    private String password;

    @Bean
    public DatabaseConnection databaseConnection() {
        return new DatabaseConnection(databaseUrl, username, password);
    }

    @Bean
    public NoteDAO noteDAO(DatabaseConnection databaseConnection) {
        // In memory, so the pages work without a database.
        // Database version: return new NoteDaoJdbc(databaseConnection); (plus its import), after running schema.sql.
        return new NoteDaoMemory();
    }
}
