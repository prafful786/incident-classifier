package com.incident.classifier.model;

import jakarta.persistence.*;

@Entity
@Table(name = "classified_chunks")
public class ClassifiedChunk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String textChunk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_topic_id")
    private Topic assignedTopic;

    @Column
    private Double confidenceScore;

    public Long getId() { return id; }
    public Document getDocument() { return document; }
    public void setDocument(Document document) { this.document = document; }
    public String getTextChunk() { return textChunk; }
    public void setTextChunk(String textChunk) { this.textChunk = textChunk; }
    public Topic getAssignedTopic() { return assignedTopic; }
    public void setAssignedTopic(Topic assignedTopic) { this.assignedTopic = assignedTopic; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
}