package com.example.api.article;

import java.util.List;

public interface ArticleService {
    Article createArticle(Article article);
    Article getArticleById(int id);
    List<Article> getAllArticles();
    Article updateArticle(int id, ArticleRequestDto articleRequestDto);
    void deleteArticle(int id);
}
