package ca.kodingkrafters.inc.vcms.backend.service.platforms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TwitterPoster implements PlatformPoster {

    @Override
    public String getPlatformName() {
        return "X"; // Match your PostRequest platform name
    }

    @Override
    public void post(String message, List<String> mediaUrls) {
        log.info("Posting to Twitter: message={}, mediaUrls={}", message, mediaUrls);
        // TODO: Add Twitter API integration here
    }
}
