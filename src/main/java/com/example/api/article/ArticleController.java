package com.example.api.article;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;


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
            @ApiResponse(responseCode = "201", description = "Article created successfully"),
            @ApiResponse(responseCode = "400", description = "Request is invalid"),
            @ApiResponse(responseCode = "409", description = "Article with the same description already exists")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponseDto createArticle(@RequestBody ArticleRequestDto articleRequestDto) {
        Article article = dtoMapper.toEntity(articleRequestDto);
        Article createdArticle = articleService.createArticle(article);
        return dtoMapper.toDto(createdArticle);
    }

    @Operation(summary = "Get an Article by ID", description = "Fetches an article by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article found"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @GetMapping("/{id}")
    public ArticleResponseDto getArticleById(@PathVariable int id) {
        Article article = articleService.getArticleById(id);
        return dtoMapper.toDto(article);
    }

    @Operation(summary = "Get all Articles", description = "Fetches all articles in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping
    public List<ArticleResponseDto> getAllArticles() {
        return articleService.getAllArticles()
                .stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    @Operation(summary = "Update an Article", description = "Updates an existing article by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article updated successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @PutMapping("/{id}")
    public ArticleResponseDto updateArticle(@PathVariable int id, @Valid @RequestBody ArticleRequestDto articleRequestDto) {
        Article updatedArticle = articleService.updateArticle(id, articleRequestDto);
        return dtoMapper.toDto(updatedArticle);
    }

    @Operation(summary = "Delete an Article", description = "Deletes an article by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Article deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Article not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable int id) {
        articleService.deleteArticle(id);
    }




}
