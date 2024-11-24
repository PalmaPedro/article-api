package com.example.api.artice;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Article Management", description = "Operations related to managing articles")
@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final DtoMapper dtoMapper;

    public ArticleController(ArticleService articleService, DtoMapper dtoMapper) {
        this.articleService = articleService;
        this.dtoMapper = dtoMapper;
    }

    @Operation(summary = "Create an Article", description = "Add a new article to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping
    public ArticleResponseDto createArticle(@RequestBody ArticleRequestDto articleRequestDto) {
        Article article = dtoMapper.toEntity(articleRequestDto);
        Article createdArticle = articleService.createArticle(article);
        return dtoMapper.toDto(createdArticle);
    }


}
