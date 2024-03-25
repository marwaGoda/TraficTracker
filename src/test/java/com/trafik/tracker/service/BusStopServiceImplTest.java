package com.trafik.tracker.service;


import com.trafik.tracker.api.BusLine;
import com.trafik.tracker.api.BusStop;
import com.trafik.tracker.integration.SLTrafficApiClient;
import com.trafik.tracker.integration.model.LineDataApiResponse;
import com.trafik.tracker.integration.model.LineJourney;
import com.trafik.tracker.integration.model.ResponseData;
import com.trafik.tracker.integration.model.StopResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BusStopServiceImplTest {

    @Mock
    private SLTrafficApiClient trafficApiClient;

    @InjectMocks
    private BusStopServiceImpl busStopService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTop10LinesWithMostStops_NoDataAvailable() {
        // Arrange
        List<BusLine> expectedLines = Collections.emptyList();
        when(trafficApiClient.fetchJourneyData()).thenReturn(Optional.of(new LineDataApiResponse<>()));
        when(trafficApiClient.fetchStopsResponse()).thenReturn(Optional.of(Collections.emptyList()));

        // Act
        List<BusLine> actualLines = busStopService.getTop10LinesWithMostStops();

        // Assert
        assertThat(actualLines).isEqualTo(expectedLines);
    }


    @Test
    void getDetailedBusLineWithStops_NoDataAvailable() {
        // Arrange
        List<BusLine> expectedLines = Collections.emptyList();
        Optional<LineDataApiResponse<LineJourney>> journeyData =
                Optional.of(new LineDataApiResponse<>());
       Optional<List<StopResponse>> stops =
                Optional.of(Collections.emptyList());
        when(trafficApiClient.fetchJourneyData()).thenReturn(journeyData);
        when(trafficApiClient.fetchStopsResponse()).thenReturn(stops);

        // Act
        List<BusLine> actualLines = busStopService.getDetailedBusLineWithStops();

        // Assert
        assertThat(actualLines).isEqualTo(expectedLines);
    }
    @Test
    void getTop10LinesWithMostStops_SampleData() {
        // Arrange
        List<BusLine> expectedLines = Arrays.asList(
                new BusLine(1, 1, generateBusStops(1))
        );
        LineDataApiResponse<LineJourney> journeyData = new LineDataApiResponse<>();
        List<LineJourney> lineJourneyList = IntStream.rangeClosed(1,1)
                .mapToObj(i -> new LineJourney(i, 1, i, "2022-02-15 00:00:00.000", "2022-02-15 00:00:00.000"))
                .collect(Collectors.toList());
        ResponseData<LineJourney> responseData = new ResponseData<>("2024-03-16 00:15", "JourneyPatternPointOnLine", lineJourneyList);
        journeyData.setResponseData(responseData);
        when(trafficApiClient.fetchJourneyData()).thenReturn(Optional.of(journeyData));
        when(trafficApiClient.fetchStopsResponse()).thenReturn(Optional.of(generateStopResponses()));

        // Act
        List<BusLine> actualLines = busStopService.getTop10LinesWithMostStops();

        // Assert
        assertThat(actualLines).isEqualTo(expectedLines);
    }


    private List<BusStop> generateBusStops(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> new BusStop(i,  i,  i, "sname" + i, "name" + i))
                .collect(Collectors.toList());
    }

    private List<StopResponse> generateStopResponses() {
        return IntStream.rangeClosed(1, 1)
                .mapToObj(i -> new StopResponse(i,  i,  i, "name" + i, "sname" + i))
                .collect(Collectors.toList());
    }

}
