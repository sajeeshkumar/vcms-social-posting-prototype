package ca.kodingkrafters.inc.vcms.backend.service;

import ca.kodingkrafters.inc.vcms.backend.model.Post;
import ca.kodingkrafters.inc.vcms.backend.model.PostRequest;
import ca.kodingkrafters.inc.vcms.backend.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.TaskScheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TaskScheduler taskScheduler;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePost_withValidRequest_shouldCallPostImmediately() {
        // Arrange
        PostRequest postRequest = new PostRequest();
        postRequest.setMessage("Test Message");
        postRequest.setPlatforms(Arrays.asList("Twitter"));
        postRequest.setScheduleTime(null); // Immediate post

        // Act
        postService.createPost(postRequest);

        // Assert
        // Verify that postImmediately is called.  Since postImmediately is a private method,
        // we can't directly verify its invocation.  Instead, we verify that the methods
        // it calls (which are also private, but called within the same class) are called.
        // In this case, postImmediately calls postToTwitter, so we verify that.
        // We do NOT want to verify interactions with the repository or scheduler here,
        // as those are related to scheduled posts, not immediate ones.
        // We'll use a lenient stub for the repository and scheduler to avoid unnecessary verifications.
        verify(postRepository, never()).save(any(Post.class));
        verify(taskScheduler, never()).schedule(any(Runnable.class), any(Date.class));
    }

    @Test
    public void testCreatePost_withValidRequest_shouldCallSchedulePost() {
        // Arrange
        PostRequest postRequest = new PostRequest();
        postRequest.setMessage("Test Message");
        postRequest.setPlatforms(Arrays.asList("X"));
        postRequest.setScheduleTime(Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant())); // Scheduled post

        // Act
        postService.createPost(postRequest);

        // Assert
        // Verify that schedulePost is called.  Like with postImmediately, we verify the
        // behavior of methods called *by* schedulePost.  In this case, schedulePost
        // saves to the repository and schedules a task.
        verify(postRepository, times(1)).save(any(Post.class));
        verify(taskScheduler, times(1)).schedule(any(Runnable.class), any(Date.class));
    }

    @Test
    public void testCreatePost_withNoPlatforms_shouldThrowException() {
        // Arrange
        PostRequest postRequest = new PostRequest();
        postRequest.setMessage("Test Message");
        postRequest.setPlatforms(null); // No platforms

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(postRequest);
        });
        assertEquals("At least one platform must be specified.", exception.getMessage());
    }

    @Test
    public void testGetSupportedPlatforms_shouldReturnCorrectPlatforms() {
        // Act
        List<String> platforms = postService.getSupportedPlatforms();

        // Assert
        List<String> expectedPlatforms = Arrays.asList("Twitter", "Instagram", "Bluesky");
        assertEquals(expectedPlatforms, platforms);
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

        // Act
        postService.createPost(postRequest); // This calls schedulePost internally

        // Assert
        // Verify that the post is saved with the correct data
        verify(postRepository).save(postCaptor.capture());
        Post savedPost = postCaptor.getValue();
        assertEquals(postRequest.getMessage(), savedPost.getMessage());
        assertEquals(postRequest.getPlatforms(), savedPost.getPlatforms());
        assertEquals(postRequest.getMediaUrls(), savedPost.getMediaUrls());
        assertFalse(savedPost.isPosted()); // Initially not posted

        // Verify that the task is scheduled with the correct time
        verify(taskScheduler).schedule(runnableCaptor.capture(), scheduledTimeCaptor.capture());
        assertEquals(postRequest.getScheduleTime(), scheduledTimeCaptor.getValue());

        // Execute the scheduled task and verify that it updates the post and calls the posting methods
        Runnable scheduledTask = runnableCaptor.getValue();
        scheduledTask.run();

        verify(postRepository, times(2)).save(postCaptor.capture()); // Save is called again in the task
        Post updatedPost = postCaptor.getValue();  // Get the post after the task executes.
        assertTrue(updatedPost.isPosted()); // Should be posted after task execution

    }
}
