package ca.kodingkrafters.inc.vcms.backend.service;

import ca.kodingkrafters.inc.vcms.backend.model.Platform;
import ca.kodingkrafters.inc.vcms.backend.repository.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {

    @Mock
    private PlatformRepository platformRepository;

    @InjectMocks
    private PlatformService platformService;

    @Test
    void getSupportedPlatforms_shouldReturnListOfPlatformNames() {
        // Arrange
        List<Platform> platforms = Arrays.asList(
                new Platform(1, "Web"),
                new Platform(2, "Mobile"),
                new Platform(3, "Desktop")
        );
        when(platformRepository.findAll()).thenReturn(platforms);

        // Act
        List<String> supportedPlatforms = platformService.getSupportedPlatforms();

        // Assert
        assertEquals(Arrays.asList("Web", "Mobile", "Desktop"), supportedPlatforms);
    }

    @Test
    void getSupportedPlatforms_shouldReturnEmptyListWhenNoPlatformsExist() {
        // Arrange
        when(platformRepository.findAll()).thenReturn(List.of());

        // Act
        List<String> supportedPlatforms = platformService.getSupportedPlatforms();

        // Assert
        assertEquals(List.of(), supportedPlatforms);
    }
}