package com.example.api.article;

public record ArticleResponseDto(
        int id,
        String description,
        double weight,
        double volume
) {
}
