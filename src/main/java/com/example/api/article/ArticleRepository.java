package com.example.api.article;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class simulates a repository for managing Article entities in memory.
 * It acts as a mock implementation of a typical Spring Data JPA repository.

 * The repository provides methods for basic CRUD operations, such as saving,
 * finding, and deleting articles, while maintaining the data in an in-memory list.

 * This implementation is designed for demonstration purposes and can be
 * replaced with a real database-backed repository (e.g., using Spring Data JPA)
 * if needed in the future.
 */
@Repository
public class ArticleRepository {
    private final List<Article> articles = new ArrayList<>();
    private int currentId = 1;

    public Article save(Article article) {
        if (article.getId() == 0) {
            article.setId(currentId++);
        }
        articles.add(article);
        return article;
    }

    public Optional<Article> findById(int id) {
        return articles.stream()
                .filter(article -> article.getId() == id)
                .findFirst();
    }

    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    public boolean deleteById(int id) {
        return articles.removeIf(article -> article.getId() == id);
    }

    public boolean existsByDescription(String description) {
        return articles.stream()
                .anyMatch(article -> article.getDescription().equalsIgnoreCase(description));
    }
}
