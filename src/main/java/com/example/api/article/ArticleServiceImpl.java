package com.example.api.article;

import com.example.api.exception.BadRequestException;
import com.example.api.exception.DuplicateResourceException;
import com.example.api.exception.ResourceNotFoundException;
import com.example.api.utility.NotificationService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{
    private final ArticleRepository articleRepository;
    private final NotificationService notificationService;

    public ArticleServiceImpl(ArticleRepository articleRepository, NotificationService notificationService) {
        this.articleRepository = articleRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Article createArticle(Article article) {
        validateArticle(article.getDescription(), article.getWeight(), article.getVolume());

        if (articleRepository.existsByDescription(article.getDescription())) {
            throw new DuplicateResourceException("An article with the description '" + article.getDescription() + "' already exists.");
        }

        Article saved = articleRepository.save(article);
        notificationService.sendNotification("Article created: [%s]".formatted(saved.getDescription()));
        return saved;
    }

    @Override
    public Article getArticleById(int id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article with id [%s] not found".formatted(id)));
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article updateArticle(int id, ArticleRequestDto dto) {
        validateArticle(dto.description(), dto.weight(), dto.volume());
        return articleRepository.findById(id)
                .map(existingArticle -> updateExistingArticle(existingArticle, dto))
                .orElseThrow(() -> new ResourceNotFoundException("Article not updated. Id [%s] not found".formatted(id)));
    }

    private Article updateExistingArticle(Article existingArticle, ArticleRequestDto dto) {
        existingArticle.setDescription(dto.description());
        existingArticle.setWeight(dto.weight());
        existingArticle.setVolume(dto.volume());
        notificationService.sendNotification("Article updated: [%s]".formatted(existingArticle.getDescription()));
        return existingArticle;
    }

    @Override
    public void deleteArticle(int id) {
        if (!articleRepository.deleteById(id)) {
            throw new ResourceNotFoundException("Article not deleted. Id [%s] not found".formatted(id));
        }
        notificationService.sendNotification("Article deleted with ID: [%s] ".formatted(id));
    }

    private void validateArticle(String description, double weight, double volume) {
        if (description == null || description.trim().isEmpty()) {
            throw new BadRequestException("Description must not be empty");
        }
        if (weight <= 0) {
            throw new BadRequestException("Weight must be greater than 0.0");
        }
        if (volume <= 0) {
            throw new BadRequestException("Volume must be greater than 0.0");
        }
    }
}
