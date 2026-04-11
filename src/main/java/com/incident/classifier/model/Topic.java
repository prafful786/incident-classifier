package com.incident.classifier.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String keywordsRaw;

    @Transient
    public List<String> getKeywordList() {
        if (keywordsRaw == null || keywordsRaw.isBlank()) return List.of();
        return List.of(keywordsRaw.split(","));
    }

    public void setKeywords(List<String> keywords) {
        this.keywordsRaw = String.join(",", keywords);
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getKeywordsRaw() { return keywordsRaw; }
    public void setKeywordsRaw(String keywordsRaw) { this.keywordsRaw = keywordsRaw; }
}