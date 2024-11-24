package com.example.api.artice;

import com.example.api.exception.DuplicateResourceException;
import com.example.api.utility.NotificationService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

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

        // Notify about the created article
        notificationService.sendNotification("Article created: " + article.getDescription());

        return article;
    }
}
