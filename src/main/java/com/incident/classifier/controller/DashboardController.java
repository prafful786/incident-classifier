package com.incident.classifier.controller;

import com.incident.classifier.dto.DashboardResponse;
import com.incident.classifier.repository.ClassifiedChunkRepository;
import com.incident.classifier.repository.DocumentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DocumentRepository documentRepository;
    private final ClassifiedChunkRepository chunkRepository;

    public DashboardController(DocumentRepository documentRepository,
                               ClassifiedChunkRepository chunkRepository) {
        this.documentRepository = documentRepository;
        this.chunkRepository = chunkRepository;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        long totalDocuments = documentRepository.count();
        long totalChunks = chunkRepository.count();
        List<Object[]> rows = chunkRepository.countGroupedByTopic();
        Map<String, Long> distribution = new LinkedHashMap<>();
        for (Object[] row : rows) {
            String topicName = (String) row[0];
            Long count = (Long) row[1];
            distribution.put(topicName, count);
        }
        return ResponseEntity.ok(new DashboardResponse(totalDocuments, totalChunks, distribution));
    }
}