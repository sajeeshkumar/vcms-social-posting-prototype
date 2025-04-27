package ca.kodingkrafters.inc.vcms.backend.service.posts;

import ca.kodingkrafters.inc.vcms.backend.model.Post;
import ca.kodingkrafters.inc.vcms.backend.model.PostRequest;
import ca.kodingkrafters.inc.vcms.backend.repository.PostRepository;
import ca.kodingkrafters.inc.vcms.backend.service.platforms.PlatformPoster;
import ca.kodingkrafters.inc.vcms.backend.service.platforms.PlatformService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.TaskScheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private PlatformService platformService;

    @Mock
    private PlatformPoster twitterPoster;

    @Mock
    private PlatformPoster instagramPoster;

    @Mock
    private PlatformPoster blueskyPoster;

    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        List<PlatformPoster> posters = Arrays.asList(twitterPoster, instagramPoster, blueskyPoster);

// Mock getPlatformName() for each poster
        when(twitterPoster.getPlatformName()).thenReturn("Twitter");
        when(instagramPoster.getPlatformName()).thenReturn("Instagram");
        when(blueskyPoster.getPlatformName()).thenReturn("Bluesky");

        postService = new PostService(platformService, postRepository, taskScheduler, posters);
    }

    @Test
    public void testCreatePost_withValidRequest_shouldCallPostImmediately() {
        // Arrange
        PostRequest postRequest = new PostRequest();
        postRequest.setMessage("Test Message");
        postRequest.setPlatforms(Arrays.asList("Twitter"));
        postRequest.setScheduleTime(null); // Immediate post
        postRequest.setMediaUrls(Arrays.asList("url1", "url2"));

        when(platformService.getSupportedPlatforms()).thenReturn(List.of("Twitter"));

        // Act
        postService.createPost(postRequest);

        // Assert
        verify(twitterPoster, times(1)).post("Test Message", Arrays.asList("url1", "url2"));
        verify(postRepository, never()).save(any(Post.class));
        verify(taskScheduler, never()).schedule(any(Runnable.class), any(Date.class));
    }

    @Test
    public void testCreatePost_withValidRequest_shouldCallSchedulePost() {
        // Arrange
        PostRequest postRequest = new PostRequest();
        postRequest.setMessage("Test Message");
        postRequest.setPlatforms(Arrays.asList("Twitter"));
        postRequest.setScheduleTime(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant()));
        postRequest.setMediaUrls(Arrays.asList("url1", "url2"));

        when(platformService.getSupportedPlatforms()).thenReturn(List.of("Twitter"));

        // Act
        postService.createPost(postRequest);

        // Assert
        verify(postRepository, times(1)).save(any(Post.class));
        verify(taskScheduler, times(1)).schedule(any(Runnable.class), any(Date.class));
    }

    @Test
    public void testCreatePost_withNoPlatforms_shouldThrowException() {
        // Arrange
        PostRequest postRequest = new PostRequest();
        postRequest.setMessage("Test Message");
        postRequest.setPlatforms(null); // No platforms

        when(platformService.getSupportedPlatforms()).thenReturn(List.of("Twitter"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(postRequest);
        });
        assertEquals("At least one platform must be specified.", exception.getMessage());
    }

    @Test
    public void testSchedulePost_shouldSavePostAndScheduleTask() {
        // Arrange
        PostRequest postRequest = new PostRequest();
        postRequest.setMessage("Test Message");
        postRequest.setPlatforms(Arrays.asList("Twitter", "Instagram"));
        postRequest.setScheduleTime(Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()));
        postRequest.setMediaUrls(Arrays.asList("url1", "url2"));

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Date> scheduledTimeCaptor = ArgumentCaptor.forClass(Date.class);

        when(platformService.getSupportedPlatforms()).thenReturn(List.of("Twitter", "Instagram"));

        // Act
        postService.createPost(postRequest);

        // Assert
        verify(postRepository).save(postCaptor.capture());
        Post savedPost = postCaptor.getValue();
        assertEquals(postRequest.getMessage(), savedPost.getMessage());
        assertEquals(postRequest.getPlatforms(), savedPost.getPlatforms());
        assertEquals(postRequest.getMediaUrls(), savedPost.getMediaUrls());
        assertFalse(savedPost.isPosted());

        verify(taskScheduler).schedule(runnableCaptor.capture(), scheduledTimeCaptor.capture());
        assertEquals(postRequest.getScheduleTime(), scheduledTimeCaptor.getValue());

        // Execute scheduled task manually
        Runnable scheduledTask = runnableCaptor.getValue();
        scheduledTask.run();

        verify(postRepository, times(2)).save(postCaptor.capture()); // Saved again after posting
        Post updatedPost = postCaptor.getValue();
        assertTrue(updatedPost.isPosted());

        verify(twitterPoster, times(1)).post("Test Message", Arrays.asList("url1", "url2"));
        verify(instagramPoster, times(1)).post("Test Message", Arrays.asList("url1", "url2"));
    }
}
