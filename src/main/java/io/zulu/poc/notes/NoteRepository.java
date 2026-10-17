package io.zulu.poc.notes;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class NoteRepository {

    private final JdbcClient jdbc;

    public NoteRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Note> findAll() {
        return jdbc.sql("SELECT id, title, content, created_at FROM notes ORDER BY id")
                .query(Note.class)
                .list();
    }

    public Optional<Note> findById(long id) {
        return jdbc.sql("SELECT id, title, content, created_at FROM notes WHERE id = :id")
                .param("id", id)
                .query(Note.class)
                .optional();
    }

    public Note insert(String title, String content) {
        return jdbc.sql("""
                        INSERT INTO notes (title, content) VALUES (:title, :content)
                        RETURNING id, title, content, created_at
                        """)
                .param("title", title)
                .param("content", content)
                .query(Note.class)
                .single();
    }

    public Optional<Note> update(long id, String title, String content) {
        return jdbc.sql("""
                        UPDATE notes SET title = :title, content = :content WHERE id = :id
                        RETURNING id, title, content, created_at
                        """)
                .param("id", id)
                .param("title", title)
                .param("content", content)
                .query(Note.class)
                .optional();
    }

    public boolean delete(long id) {
        return jdbc.sql("DELETE FROM notes WHERE id = :id")
                .param("id", id)
                .update() > 0;
    }
}
