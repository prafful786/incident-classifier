package com.incident.classifier.controller;

import com.incident.classifier.dto.DocumentResultResponse;
import com.incident.classifier.model.Document;
import com.incident.classifier.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "File must not be empty"));
        }
        Document doc = documentService.processFile(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "documentId", doc.getId(),
                        "message", "Document uploaded and classified successfully",
                        "createdAt", doc.getCreatedAt().toString()
                ));
    }

    @PostMapping(value = "/text", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> uploadText(
            @RequestBody Map<String, String> body) {
        String text = body.get("text");
        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "'text' field must not be empty"));
        }
        Document doc = documentService.processRawText(text);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "documentId", doc.getId(),
                        "message", "Text classified successfully",
                        "createdAt", doc.getCreatedAt().toString()
                ));
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<DocumentResultResponse> getResults(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getResults(id));
    }
}