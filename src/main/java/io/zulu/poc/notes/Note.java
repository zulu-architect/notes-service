package io.zulu.poc.notes;

import java.time.Instant;

public record Note(Long id, String title, String content, Instant createdAt) {
}
