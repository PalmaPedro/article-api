package com.example.api.article;

import com.example.api.exception.ResourceNotFoundException;
import com.example.api.utility.NotificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceImplTest {
    @Mock
    private NotificationService notificationService;
    @Mock
    private ArticleRepository articleRepository;
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

        Article savedArticle = new Article();
        savedArticle.setId(1); // Simulating auto-increment ID
        savedArticle.setDescription("New Article");
        savedArticle.setWeight(10.5);
        savedArticle.setVolume(5.0);

        // Mock behavior
        when(articleRepository.existsByDescription(article.getDescription())).thenReturn(false);
        when(articleRepository.save(article)).thenReturn(savedArticle);

        // Act
        Article createdArticle = articleService.createArticle(article);

        // Assert
        assertEquals("New Article", createdArticle.getDescription());
        assertEquals(10.5, createdArticle.getWeight());
        assertEquals(5.0, createdArticle.getVolume());
        assertEquals(1, createdArticle.getId());

        // Verify notification
        verify(notificationService, times(1)).sendNotification("Article created: [New Article]");
    }

    @Test
    void testGetArticleById_Success() {
        // Arrange
        int articleId = 1;
        Article article = new Article();
        article.setId(articleId);
        article.setDescription("Existing Article");
        article.setWeight(10.0);
        article.setVolume(5.0);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // Act
        Article foundArticle = articleService.getArticleById(articleId);

        // Assert
        assertNotNull(foundArticle);
        assertEquals(articleId, foundArticle.getId());
        assertEquals("Existing Article", foundArticle.getDescription());
    }

    @Test
    void testGetArticleById_NotFound() {
        // Arrange
        int nonExistentId = 999;
        when(articleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> articleService.getArticleById(nonExistentId));
    }

    @Test
    void testUpdateArticle_Success() {
        // Arrange
        int articleId = 1;
        Article existingArticle = new Article();
        existingArticle.setId(articleId);
        existingArticle.setDescription("Old Description");
        existingArticle.setWeight(5.0);
        existingArticle.setVolume(3.0);

        ArticleRequestDto updateDto = new ArticleRequestDto("Updated Description", 7.5, 4.0);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));

        // Act
        Article updatedArticle = articleService.updateArticle(articleId, updateDto);

        // Assert
        assertNotNull(updatedArticle);
        assertEquals("Updated Description", updatedArticle.getDescription());
        assertEquals(7.5, updatedArticle.getWeight());
        assertEquals(4.0, updatedArticle.getVolume());

        verify(notificationService, times(1)).sendNotification("Article updated: [Updated Description]");
    }

    @Test
    void testUpdateArticle_NotFound() {
        // Arrange
        int nonExistentId = 999;
        ArticleRequestDto updateDto = new ArticleRequestDto("Updated Description", 7.5, 4.0);

        when(articleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> articleService.updateArticle(nonExistentId, updateDto));
    }

    @Test
    void testDeleteArticle_Success() {
        // Arrange
        int articleId = 1;
        Article article = new Article();
        article.setId(articleId);
        article.setDescription("Article to Delete");

        when(articleRepository.deleteById(articleId)).thenReturn(true);

        // Act
        articleService.deleteArticle(articleId);

        // Assert
        verify(notificationService, times(1)).sendNotification("Article deleted with ID: [%s] ".formatted(articleId));
    }

    @Test
    void testDeleteArticle_NotFound() {
        // Arrange
        int nonExistentId = 999;

        when(articleRepository.deleteById(nonExistentId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> articleService.deleteArticle(nonExistentId));
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }


}
