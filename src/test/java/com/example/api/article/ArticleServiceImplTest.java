package com.example.api.article;

import com.example.api.exception.ResourceNotFoundException;
import com.example.api.utility.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ArticleServiceImplTest {
    @Mock
    private NotificationService notificationService;
    @InjectMocks
    private ArticleServiceImpl articleService;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateArticle_Success() {
        // Arrange
        Article article = new Article();
        article.setDescription("New Article");
        article.setWeight(10.5);
        article.setVolume(5.0);

        // Act
        Article createdArticle = articleService.createArticle(article);

        // Assert
        assertEquals("New Article", createdArticle.getDescription());
        assertEquals(10.5, createdArticle.getWeight());
        assertEquals(5.0, createdArticle.getVolume());

        // Verify notification
        verify(notificationService, times(1)).sendNotification("Article created: New Article");
    }

    @Test
    void testGetArticleById_Success() {
        // Arrange
        Article article = new Article();
        article.setDescription("Existing Article");
        article.setWeight(10.0);
        article.setVolume(5.0);
        articleService.createArticle(article);

        // Act
        Article foundArticle = articleService.getArticleById(article.getId());

        // Assert
        assertNotNull(foundArticle);
        assertEquals(article.getId(), foundArticle.getId());
        assertEquals("Existing Article", foundArticle.getDescription());
    }

    @Test
    void testGetArticleById_NotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> articleService.getArticleById(999));
    }

    @Test
    void testUpdateArticle_Success() {
        // Arrange
        Article existingArticle = new Article();
        existingArticle.setDescription("Old Description");
        existingArticle.setWeight(5.0);
        existingArticle.setVolume(3.0);
        articleService.createArticle(existingArticle);

        ArticleRequestDto updateDto = new ArticleRequestDto("Updated Description", 7.5, 4.0);

        // Act
        Article updatedArticle = articleService.updateArticle(existingArticle.getId(), updateDto);

        // Assert
        assertNotNull(updatedArticle);
        assertEquals("Updated Description", updatedArticle.getDescription());
        assertEquals(7.5, updatedArticle.getWeight());
        assertEquals(4.0, updatedArticle.getVolume());

        // Verify notification
        verify(notificationService, times(1)).sendNotification("Article updated: Updated Description");
    }

    @Test
    void testUpdateArticle_NotFound() {
        // Arrange
        int nonExistentId = 999;
        ArticleRequestDto updateDto = new ArticleRequestDto("Updated Description", 7.5, 4.0);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> articleService.updateArticle(nonExistentId, updateDto));
    }

    @Test
    void testDeleteArticle_Success() {
        // Arrange
        Article article = new Article();
        article.setDescription("Article to Delete");
        articleService.createArticle(article);

        // Act
        articleService.deleteArticle(article.getId());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> articleService.getArticleById(article.getId()));

        // Verify notification
        verify(notificationService, times(1)).sendNotification("Article deleted with ID: " + article.getId());

    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }


}
