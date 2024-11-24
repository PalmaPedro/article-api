package com.example.api.artice;

import com.example.api.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping
    public ArticleResponseDto createArticle(@RequestBody ArticleRequestDto articleRequestDto) {
        Article article = dtoMapper.toEntity(articleRequestDto);
        Article createdArticle = articleService.createArticle(article);
        return dtoMapper.toDto(createdArticle);
    }

    @Operation(summary = "Get an article by id", description = "Fetches an article by article id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("/{id}")
    public ArticleResponseDto getArticleById(@PathVariable int id) {
        Article article = articleService.getArticle(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "article with id [%s] not found".formatted(id)));
        return dtoMapper.toDto(article);
    }

    @Operation(summary = "Get all articles", description = "Fetches all articles from the list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("/")
    public List<ArticleResponseDto> getAllArticles() {
        return articleService.getAllArticles()
                .stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    @Operation(summary = "Update an article", description = "Updates an article by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PutMapping("/{id}")
    public ArticleResponseDto updateArticle(@PathVariable int id, @RequestBody ArticleRequestDto articleRequestDto) {
        Article updatedArticle = articleService.updateArticle(id, articleRequestDto)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with ID: " + id));
        return dtoMapper.toDto(updatedArticle);
    }

    @Operation(summary = "Delete an article", description = "Deletes an article by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable int id) {
        if (!articleService.deleteArticle(id)) {
            throw new ResourceNotFoundException("Article not found with ID: " + id);
        }
    }




}
