package ca.kodingkrafters.inc.vcms.backend.service.platforms;

import java.util.List;

public interface PlatformPoster {
    String getPlatformName();
    void post(String message, List<String> mediaUrls);
}
