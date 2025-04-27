package ca.kodingkrafters.inc.vcms.backend.service;


import ca.kodingkrafters.inc.vcms.backend.model.Post;
import ca.kodingkrafters.inc.vcms.backend.model.PostRequest;
import ca.kodingkrafters.inc.vcms.backend.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PostService {

    private final PlatformService platformService;
    private final PostRepository postRepository;
    private final TaskScheduler taskScheduler;

    @Autowired
    public PostService(PlatformService platformService, PostRepository postRepository, TaskScheduler taskScheduler) {
        this.platformService = platformService;
        this.postRepository = postRepository;
        this.taskScheduler = taskScheduler;
    }

    public void createPost(PostRequest postRequest) {
        String message = postRequest.getMessage();
        List<String> mediaUrls = postRequest.getMediaUrls();
        List<String> platforms = postRequest.getPlatforms();
        Date scheduleTime = postRequest.getScheduleTime();

        log.info("Received post request: message={}, mediaUrls={}, platforms={}, scheduleTime={}", message, mediaUrls, platforms, scheduleTime);

        if (platforms == null || platforms.isEmpty()) {
            throw new IllegalArgumentException("At least one platform must be specified.");
        }

        List<String> supportedPlatforms = platformService.getSupportedPlatforms();

        for (String platform : platforms) {
            if (!supportedPlatforms.contains(platform)) {
                log.warn("Platform '{}' is not supported.", platform);
                // Consider throwing an exception or handling this case appropriately
                // depending on your application's requirements.  For example:
                // throw new IllegalArgumentException("Platform '" + platform + "' is not supported.");
            }
        }

        if (scheduleTime != null) {
            schedulePost(postRequest); // handle scheduling
        } else {
            postImmediately(postRequest); // handle immediate post
        }
    }

    private void postImmediately(PostRequest postRequest) {
        List<String> platforms = postRequest.getPlatforms();
        String message = postRequest.getMessage();
        List<String> mediaUrls = postRequest.getMediaUrls();

        for (String platform : platforms) {
            switch (platform) {
                case "X":
                    postToTwitter(message, mediaUrls);
                    break;
                case "Instagram":
                    postToInstagram(message, mediaUrls);
                    break;
                case "Bluesky":
                    postToBluesky(message, mediaUrls);
                    break;
                default:
                    log.warn("Platform '{}' is not supported.", platform);
            }
        }
    }

    private void schedulePost(PostRequest postRequest) {
        List<String> platforms = postRequest.getPlatforms();
        String message = postRequest.getMessage();
        List<String> mediaUrls = postRequest.getMediaUrls();
        Date scheduleTime = postRequest.getScheduleTime();

        // Save the post to the database
        Post post = new Post();
        post.setId(UUID.randomUUID()); // Use UUID for IDs
        post.setMessage(message);
        post.setMediaUrls(mediaUrls);
        post.setPlatforms(platforms);
        post.setScheduledTime(Instant.ofEpochMilli(postRequest.getScheduleTime().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        post.setPosted(false); // Initially not posted
        postRepository.save(post);

        // Schedule the post using TaskScheduler
        taskScheduler.schedule(() -> {
            log.info("Executing scheduled post for id: {}", post.getId());
            post.setPosted(true);
            postRepository.save(post); //update the post to mark it as posted.
            for (String platform : platforms) {
                switch (platform) {
                    case "X":
                        postToTwitter(message, mediaUrls);
                        break;
                    case "Instagram":
                        postToInstagram(message, mediaUrls);
                        break;
                    case "Bluesky":
                        postToBluesky(message, mediaUrls);
                        break;
                    default:
                        log.warn("Platform '{}' is not supported.", platform);
                }
            }
        }, scheduleTime);
        log.info("Post scheduled successfully for {} for id: {}", scheduleTime, post.getId());
    }

    private void postToTwitter(String message, List<String> mediaUrls) {
        // Implementation for posting to Twitter immediately
        log.info("Posting to Twitter: message={}, mediaUrls={}", message, mediaUrls);
        // Add your Twitter API integration code here
    }

    private void postToInstagram(String message, List<String> mediaUrls) {
        // Implementation for posting to Instagram immediately
        log.info("Posting to Instagram: message={}, mediaUrls={}", message, mediaUrls);
        // Add your Instagram API integration code here
    }

    private void postToBluesky(String message, List<String> mediaUrls) {
        // Implementation for posting to Bluesky immediately
        log.info("Posting to Bluesky: message={}, mediaUrls={}", message, mediaUrls);
        // Add your Bluesky API integration code here
    }
}
