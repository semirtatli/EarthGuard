package com.earthguard.earthquake.service;

import com.earthguard.common.entity.Earthquake;
import com.earthguard.common.enums.AlertLevel;
import com.earthguard.earthquake.repository.EarthquakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EarthquakeService Tests")
class EarthquakeServiceImplTest {

    @Mock
    private EarthquakeRepository repository;

    @InjectMocks
    private EarthquakeServiceImpl service;

    private Earthquake testEarthquake;

    @BeforeEach
    void setUp() {
        testEarthquake = new Earthquake();
        testEarthquake.setId("test001");
        testEarthquake.setMagnitude(5.5);
        testEarthquake.setLatitude(41.0);
        testEarthquake.setLongitude(29.0);
        testEarthquake.setDepth(10);
        testEarthquake.setLocation("Istanbul, Turkey");
        testEarthquake.setTimestamp(LocalDateTime.now());
        testEarthquake.setAlertLevel(AlertLevel.MODERATE);
    }

    @Test
    @DisplayName("Should save earthquake successfully")
    void testSave() {
        // Given
        when(repository.save(any(Earthquake.class))).thenReturn(testEarthquake);

        // When
        Earthquake result = service.save(testEarthquake);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("test001");
        assertThat(result.getMagnitude()).isEqualTo(5.5);

        verify(repository, times(1)).save(testEarthquake);
    }

    @Test
    @DisplayName("Should find earthquake by id")
    void testFindById() {
        // Given
        when(repository.findById("test001")).thenReturn(Optional.of(testEarthquake));

        // When
        Optional<Earthquake> result = service.findById("test001");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("test001");

        verify(repository, times(1)).findById("test001");
    }

    @Test
    @DisplayName("Should return empty when earthquake not found")
    void testFindByIdNotFound() {
        // Given
        when(repository.findById("nonexistent")).thenReturn(Optional.empty());

        // When
        Optional<Earthquake> result = service.findById("nonexistent");

        // Then
        assertThat(result).isEmpty();

        verify(repository, times(1)).findById("nonexistent");
    }

    @Test
    @DisplayName("Should find all earthquakes")
    void testFindAll() {
        // Given
        Earthquake eq2 = new Earthquake();
        eq2.setId("test002");
        eq2.setMagnitude(6.0);

        when(repository.findAll()).thenReturn(Arrays.asList(testEarthquake, eq2));

        // When
        List<Earthquake> result = service.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Earthquake::getId)
                .containsExactlyInAnyOrder("test001", "test002");

        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find earthquakes by magnitude")
    void testFindByMagnitude() {
        // Given
        when(repository.findByMagnitudeGreaterThanEqual(5.0))
                .thenReturn(List.of(testEarthquake));

        // When
        List<Earthquake> result = service.findByMagnitude(5.0);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMagnitude()).isEqualTo(5.5);

        verify(repository, times(1)).findByMagnitudeGreaterThanEqual(5.0);
    }

    @Test
    @DisplayName("Should find earthquakes by alert level")
    void testFindByAlertLevel() {
        // Given
        when(repository.findByAlertLevel(AlertLevel.MODERATE))
                .thenReturn(List.of(testEarthquake));

        // When
        List<Earthquake> result = service.findByAlertLevel(AlertLevel.MODERATE);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAlertLevel()).isEqualTo(AlertLevel.MODERATE);

        verify(repository, times(1)).findByAlertLevel(AlertLevel.MODERATE);
    }

    @Test
    @DisplayName("Should delete earthquake by id when exists")
    void testDeleteById() {
        // Given
        when(repository.existsById("test001")).thenReturn(true);
        doNothing().when(repository).deleteById("test001");

        // When
        service.deleteById("test001");

        // Then
        verify(repository, times(1)).existsById("test001");
        verify(repository, times(1)).deleteById("test001");
    }

    @Test
    @DisplayName("Should get total count")
    void testGetTotalCount() {
        // Given
        when(repository.count()).thenReturn(42L);

        // When
        long result = service.getTotalCount();

        // Then
        assertThat(result).isEqualTo(42L);

        verify(repository, times(1)).count();
    }

    @Test
    @DisplayName("Should find recent critical alerts")
    void testFindRecentCriticalAlerts() {
        // Given
        Earthquake criticalEq = new Earthquake();
        criticalEq.setId("critical001");
        criticalEq.setMagnitude(7.5);
        criticalEq.setAlertLevel(AlertLevel.CRITICAL);

        when(repository.findRecentStrongEarthquakes(eq(6.0), any(LocalDateTime.class)))
                .thenReturn(List.of(criticalEq));

        // When
        List<Earthquake> result = service.findRecentCriticalAlerts(24);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMagnitude()).isEqualTo(7.5);

        verify(repository, times(1)).findRecentStrongEarthquakes(eq(6.0), any(LocalDateTime.class));
    }
}