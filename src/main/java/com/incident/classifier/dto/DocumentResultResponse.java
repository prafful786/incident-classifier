package com.incident.classifier.dto;

import java.util.List;

public class DocumentResultResponse {

    private Long documentId;
    private List<ChunkResult> results;

    public DocumentResultResponse(Long documentId, List<ChunkResult> results) {
        this.documentId = documentId;
        this.results = results;
    }

    public Long getDocumentId() { return documentId; }
    public List<ChunkResult> getResults() { return results; }

    public static class ChunkResult {
        private String text;
        private String assignedTopic;
        private Double confidence;

        public ChunkResult(String text, String assignedTopic, Double confidence) {
            this.text = text;
            this.assignedTopic = assignedTopic;
            this.confidence = confidence;
        }

        public String getText() { return text; }
        public String getAssignedTopic() { return assignedTopic; }
        public Double getConfidence() { return confidence; }
    }
}