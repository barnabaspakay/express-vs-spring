package com.codecool.notes.configuration;

import com.codecool.notes.dao.NoteDAO;
import com.codecool.notes.dao.NoteDaoMemory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class Configuration {

    @Bean
    public NoteDAO noteDAO() {
        return new NoteDaoMemory();
    }
}
