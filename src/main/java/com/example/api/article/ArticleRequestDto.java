package com.example.api.article;

public record ArticleRequestDto(
        String description,
        double weight,
        double volume
) {
}
