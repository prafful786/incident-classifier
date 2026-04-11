package com.incident.classifier.service;

import com.incident.classifier.dto.TopicRequest;
import com.incident.classifier.exception.ResourceNotFoundException;
import com.incident.classifier.model.Topic;
import com.incident.classifier.repository.TopicRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic createTopic(TopicRequest request) {
        topicRepository.findByTitleIgnoreCase(request.getTitle()).ifPresent(t -> {
            throw new IllegalArgumentException("Topic with title '" + request.getTitle() + "' already exists.");
        });
        Topic topic = new Topic();
        topic.setTitle(request.getTitle());
        topic.setKeywords(request.getKeywords());
        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
    }
}