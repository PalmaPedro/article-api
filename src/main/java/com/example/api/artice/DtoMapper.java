package com.example.api.artice;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public Article toEntity(ArticleRequestDto dto) {
        Article article = new Article();
        article.setDescription(dto.description());
        article.setWeight(dto.weight());
        article.setVolume(dto.volume());

        return article;
    }

    public ArticleResponseDto toDto(Article article) {
        return new ArticleResponseDto(article.getId(), article.getDescription(), article.getWeight(), article.getVolume());
    }

}
