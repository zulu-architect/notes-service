package io.zulu.poc.notes;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteRepository notes;

    public NoteController(NoteRepository notes) {
        this.notes = notes;
    }

    @GetMapping
    public List<Note> list() {
        return notes.findAll();
    }

    @GetMapping("/{id}")
    public Note get(@PathVariable long id) {
        return notes.findById(id).orElseThrow(() -> notFound(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Note create(@Valid @RequestBody NoteRequest request) {
        return notes.insert(request.title(), contentOrEmpty(request));
    }

    @PutMapping("/{id}")
    public Note update(@PathVariable long id, @Valid @RequestBody NoteRequest request) {
        return notes.update(id, request.title(), contentOrEmpty(request))
                .orElseThrow(() -> notFound(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        if (!notes.delete(id)) {
            throw notFound(id);
        }
    }

    private static String contentOrEmpty(NoteRequest request) {
        return request.content() == null ? "" : request.content();
    }

    private static ResponseStatusException notFound(long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Note " + id + " not found");
    }
}
