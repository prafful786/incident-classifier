package com.incident.classifier.service;

import com.incident.classifier.model.Topic;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassificationService {

    public record ClassificationResult(Topic topic, double confidence) {}

    public ClassificationResult classify(String text, List<Topic> topics) {
        if (topics == null || topics.isEmpty()) {
            return new ClassificationResult(null, 0.0);
        }

        String lowerText = text.toLowerCase();
        Map<Topic, Double> scores = new HashMap<>();

        for (Topic topic : topics) {
            List<String> keywords = topic.getKeywordList();
            if (keywords.isEmpty()) continue;

            long matchCount = keywords.stream()
                    .filter(kw -> lowerText.contains(kw.toLowerCase().trim()))
                    .count();

            double confidence = (double) matchCount / keywords.size();
            scores.put(topic, confidence);
        }

        return scores.entrySet().stream()
                .filter(e -> e.getValue() > 0.0)
                .max(Map.Entry.comparingByValue())
                .map(e -> new ClassificationResult(e.getKey(), e.getValue()))
                .orElse(new ClassificationResult(null, 0.0));
    }

    public List<String> splitIntoChunks(String text) {
        String[] parts = text.split("(?<=[.!?])\\s+");
        return List.of(parts).stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}