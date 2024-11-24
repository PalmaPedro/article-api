package com.example.api.artice;

import java.util.List;
import java.util.Optional;

public interface ArticleService {
    Article createArticle(Article article);
    Optional<Article> getArticle(Integer id);
    List<Article> getAllArticles();
}
