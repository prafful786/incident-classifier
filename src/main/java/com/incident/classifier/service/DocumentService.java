package com.incident.classifier.service;

import com.incident.classifier.dto.DocumentResultResponse;
import com.incident.classifier.exception.ResourceNotFoundException;
import com.incident.classifier.model.ClassifiedChunk;
import com.incident.classifier.model.Document;
import com.incident.classifier.model.Topic;
import com.incident.classifier.repository.ClassifiedChunkRepository;
import com.incident.classifier.repository.DocumentRepository;
import com.incident.classifier.repository.TopicRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TopicRepository topicRepository;
    private final ClassifiedChunkRepository chunkRepository;
    private final ClassificationService classificationService;

    public DocumentService(DocumentRepository documentRepository,
                           TopicRepository topicRepository,
                           ClassifiedChunkRepository chunkRepository,
                           ClassificationService classificationService) {
        this.documentRepository = documentRepository;
        this.topicRepository = topicRepository;
        this.chunkRepository = chunkRepository;
        this.classificationService = classificationService;
    }

    public Document processFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String text;
        if (contentType != null && contentType.equalsIgnoreCase("application/pdf")) {
            text = extractTextFromPdf(file);
        } else {
            text = new String(file.getBytes());
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Uploaded file is empty or could not be read.");
        }
        return classifyAndSave(text);
    }

    public Document processRawText(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            throw new IllegalArgumentException("Raw text must not be empty.");
        }
        return classifyAndSave(rawText);
    }

    public DocumentResultResponse getResults(Long documentId) {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + documentId));
        List<ClassifiedChunk> chunks = chunkRepository.findByDocumentId(doc.getId());
        List<DocumentResultResponse.ChunkResult> results = chunks.stream()
                .map(chunk -> new DocumentResultResponse.ChunkResult(
                        chunk.getTextChunk(),
                        chunk.getAssignedTopic() != null ? chunk.getAssignedTopic().getTitle() : "UNCLASSIFIED",
                        chunk.getConfidenceScore()
                ))
                .toList();
        return new DocumentResultResponse(doc.getId(), results);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument pdDocument = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(pdDocument);
        }
    }

    private Document classifyAndSave(String text) {
        Document document = new Document();
        document.setOriginalText(text);
        document = documentRepository.save(document);
        List<Topic> topics = topicRepository.findAll();
        List<String> chunks = classificationService.splitIntoChunks(text);
        List<ClassifiedChunk> classifiedChunks = new ArrayList<>();
        for (String chunkText : chunks) {
            ClassificationService.ClassificationResult result =
                    classificationService.classify(chunkText, topics);
            ClassifiedChunk chunk = new ClassifiedChunk();
            chunk.setDocument(document);
            chunk.setTextChunk(chunkText);
            chunk.setAssignedTopic(result.topic());
            chunk.setConfidenceScore(result.confidence());
            classifiedChunks.add(chunk);
        }
        chunkRepository.saveAll(classifiedChunks);
        return document;
    }
}