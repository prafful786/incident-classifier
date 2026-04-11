package com.incident.classifier.repository;

import com.incident.classifier.model.ClassifiedChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassifiedChunkRepository extends JpaRepository<ClassifiedChunk, Long> {

    List<ClassifiedChunk> findByDocumentId(Long documentId);

    long countByDocumentId(Long documentId);

    @Query("SELECT COALESCE(t.title, 'UNCLASSIFIED'), COUNT(c) " +
            "FROM ClassifiedChunk c LEFT JOIN c.assignedTopic t " +
            "GROUP BY t.title")
    List<Object[]> countGroupedByTopic();
}