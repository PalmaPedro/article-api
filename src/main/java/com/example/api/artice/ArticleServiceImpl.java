package com.example.api.artice;

import com.example.api.exception.DuplicateResourceException;
import com.example.api.utility.NotificationService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService{
    private final List<Article> articles = new ArrayList<>();
    private int currentId = 1; // Simulated auto-increment ID
    private final NotificationService notificationService;

    public ArticleServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public Article createArticle(Article article) {
        article.setId(currentId++);

        if (articles.stream().anyMatch(a -> a.getDescription().equalsIgnoreCase(article.getDescription()))) {
            throw new DuplicateResourceException("An article with the description '" + article.getDescription() + "' already exists.");
        }

        articles.add(article);
        notificationService.sendNotification("Article created: " + article.getDescription());

        return article;
    }

    @Override
    public Optional<Article> getArticle(Integer id) {
        return articles.stream()
                .filter(article -> article.getId() == id)
                .findFirst();
    }

    @Override
    public List<Article> getAllArticles() {
        return articles;
    }

    @Override
    public Optional<Article> updateArticle(int id, ArticleRequestDto dto) {
        return articles.stream()
                .filter(article -> article.getId() == id)
                .findFirst()
                .map(existingArticle -> {
                    existingArticle.setDescription(dto.description());
                    existingArticle.setWeight(dto.weight());
                    existingArticle.setVolume(dto.volume());

                    notificationService.sendNotification("Article updated: " + existingArticle.getDescription());

                    return existingArticle;
                });
    }

    @Override
    public boolean deleteArticle(int id) {
        boolean removed = articles.removeIf(article -> article.getId() == id);
        if (removed) {
            notificationService.sendNotification("Article deleted with ID: " + id);
        }
        return removed;
    }
}
