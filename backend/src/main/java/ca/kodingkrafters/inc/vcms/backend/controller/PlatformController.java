package ca.kodingkrafters.inc.vcms.backend.controller;

import ca.kodingkrafters.inc.vcms.backend.api.PlatformsApi;
import ca.kodingkrafters.inc.vcms.backend.service.platforms.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlatformController implements PlatformsApi {

    @Autowired
    private PlatformService platformService;

    @Override
    public ResponseEntity<List<String>> platformsGet() {
        return ResponseEntity.ok(platformService.getSupportedPlatforms());
    }
}
