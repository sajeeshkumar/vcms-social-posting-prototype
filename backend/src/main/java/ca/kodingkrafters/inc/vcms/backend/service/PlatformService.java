package ca.kodingkrafters.inc.vcms.backend.service;

import ca.kodingkrafters.inc.vcms.backend.model.Platform;
import ca.kodingkrafters.inc.vcms.backend.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    public List<String> getSupportedPlatforms() {
        return platformRepository.findAll().stream().map(Platform::getName).collect(Collectors.toList());
    }
}
