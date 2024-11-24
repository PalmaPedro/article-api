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
        if (articleRepository.existsByDescription(article.getDescription())) {
            throw new DuplicateResourceException("An article with the description '" + article.getDescription() + "' already exists.");
        }
        if (article.getDescription() == null || article.getDescription().trim().isEmpty()) {
            throw new BadRequestException("Description must not be empty");
        }
        if (article.getWeight() <= 0) {
            throw new BadRequestException("Weight must be greater than 0.0");
        }
        if (article.getVolume() <= 0) {
            throw new BadRequestException("Volume must be greater than 0.0");
        }

        Article saved = articleRepository.save(article);
        notificationService.sendNotification("Article created: " + saved.getDescription());
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
        return articleRepository.findById(id)
                .map(existingArticle -> updateExistingArticle(existingArticle, dto))
                .orElseThrow(() -> new ResourceNotFoundException("Article with id [%s] not found".formatted(id)));
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
            throw new ResourceNotFoundException("Article with id [%s] not found".formatted(id));
        }
        notificationService.sendNotification("Article deleted with ID: [%s] ".formatted(id));
    }
}
