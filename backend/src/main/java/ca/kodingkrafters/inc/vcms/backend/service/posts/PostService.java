package ca.kodingkrafters.inc.vcms.backend.service.posts;

import ca.kodingkrafters.inc.vcms.backend.model.Post;
import ca.kodingkrafters.inc.vcms.backend.model.PostRequest;
import ca.kodingkrafters.inc.vcms.backend.repository.PostRepository;
import ca.kodingkrafters.inc.vcms.backend.service.platforms.PlatformPoster;
import ca.kodingkrafters.inc.vcms.backend.service.platforms.PlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    private final PlatformService platformService;
    private final PostRepository postRepository;
    private final TaskScheduler taskScheduler;
    private final Map<String, PlatformPoster> posterMap; // NEW

    @Autowired
    public PostService(
            PlatformService platformService,
            PostRepository postRepository,
            TaskScheduler taskScheduler,
            List<PlatformPoster> posters // Spring will inject all implementations
    ) {
        this.platformService = platformService;
        this.postRepository = postRepository;
        this.taskScheduler = taskScheduler;
        this.posterMap = posters.stream()
                .collect(Collectors.toMap(PlatformPoster::getPlatformName, p -> p));
    }

    public void createPost(PostRequest postRequest) {
        validatePlatforms(postRequest.getPlatforms());

        if (postRequest.getScheduleTime() != null) {
            schedulePost(postRequest);
        } else {
            postImmediately(postRequest);
        }
    }

    private void validatePlatforms(List<String> platforms) {
        if (platforms == null || platforms.isEmpty()) {
            throw new IllegalArgumentException("At least one platform must be specified.");
        }
        List<String> supportedPlatforms = platformService.getSupportedPlatforms();
        for (String platform : platforms) {
            if (!supportedPlatforms.contains(platform)) {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
        }
    }

    private void postImmediately(PostRequest postRequest) {
        for (String platform : postRequest.getPlatforms()) {
            PlatformPoster poster = posterMap.get(platform);
            if (poster != null) {
                poster.post(postRequest.getMessage(), postRequest.getMediaUrls());
            } else {
                log.warn("No poster found for platform '{}'", platform);
            }
        }
    }

    private void schedulePost(PostRequest postRequest) {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setMessage(postRequest.getMessage());
        post.setMediaUrls(postRequest.getMediaUrls());
        post.setPlatforms(postRequest.getPlatforms());
        post.setScheduledTime(Instant.ofEpochMilli(postRequest.getScheduleTime().getTime())
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        post.setPosted(false);
        postRepository.save(post);

        taskScheduler.schedule(() -> {
            log.info("Executing scheduled post for id: {}", post.getId());
            post.setPosted(true);
            postRepository.save(post);
            postImmediately(postRequest);
        }, postRequest.getScheduleTime());

        log.info("Post scheduled successfully for {} for id: {}", postRequest.getScheduleTime(), post.getId());
    }
}
