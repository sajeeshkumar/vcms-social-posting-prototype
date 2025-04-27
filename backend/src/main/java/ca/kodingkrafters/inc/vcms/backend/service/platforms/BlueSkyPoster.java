package ca.kodingkrafters.inc.vcms.backend.service.platforms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class BlueSkyPoster implements PlatformPoster {

    @Override
    public String getPlatformName() {
        return "Bluesky"; // Match your PostRequest platform name
    }

    @Override
    public void post(String message, List<String> mediaUrls) {
        log.info("Posting to Bluesky: message={}, mediaUrls={}", message, mediaUrls);
        // TODO: Add Bluesky API integration here
    }
}
