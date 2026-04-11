package com.incident.classifier.dto;

import java.util.Map;

public class DashboardResponse {

    private long totalDocuments;
    private long totalChunks;
    private Map<String, Long> topicDistribution;

    public DashboardResponse(long totalDocuments, long totalChunks, Map<String, Long> topicDistribution) {
        this.totalDocuments = totalDocuments;
        this.totalChunks = totalChunks;
        this.topicDistribution = topicDistribution;
    }

    public long getTotalDocuments() { return totalDocuments; }
    public long getTotalChunks() { return totalChunks; }
    public Map<String, Long> getTopicDistribution() { return topicDistribution; }
}