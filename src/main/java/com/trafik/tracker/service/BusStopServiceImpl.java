package com.trafik.tracker.service;

import com.trafik.tracker.api.BusLine;
import com.trafik.tracker.api.BusStop;
import com.trafik.tracker.integration.SLTrafficApiClient;
import com.trafik.tracker.integration.model.LineDataApiResponse;
import com.trafik.tracker.integration.model.LineJourney;
import com.trafik.tracker.integration.model.StopResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusStopServiceImpl implements BusStopService {

    private final SLTrafficApiClient trafficApiClient;

    @Autowired
    public BusStopServiceImpl(SLTrafficApiClient trafficApiClient) {
        this.trafficApiClient = trafficApiClient;
    }

    @Override
    public List<BusLine> getTop10LinesWithMostStops() {
        List<BusLine> busLineList = getDetailedBusLineWithStops();
        return busLineList.stream()
                .sorted(Comparator.comparingInt(BusLine::getStopsCount).reversed())
                .limit(10)
                .toList();

    }

    @Override
    public List<BusLine> getDetailedBusLineWithStops() {
        CompletableFuture<Optional<LineDataApiResponse<LineJourney>>> journeyDataFuture =
                CompletableFuture.supplyAsync(trafficApiClient::fetchJourneyData);

        CompletableFuture<Optional<List<StopResponse>>> stopsFuture =
                CompletableFuture.supplyAsync(trafficApiClient::fetchStopsResponse);
        @SuppressWarnings("unchecked")
        List<BusLine> detailedResult = (List<BusLine>) CompletableFuture.allOf(journeyDataFuture, stopsFuture)
                .thenApply(v -> {
                    Optional<LineDataApiResponse<LineJourney>> journeyDataOptional = journeyDataFuture.join();
                    Optional<List<StopResponse>> stopsOptional = stopsFuture.join();

                    if (journeyDataOptional.isEmpty() || stopsOptional.isEmpty() ||
                            journeyDataOptional.get().getResponseData() == null ||
                            stopsOptional.get().isEmpty()) {
                        return Collections.emptyList();
                    }

                    LineDataApiResponse<LineJourney> lineDataApiResponse = journeyDataOptional.get();
                    List<StopResponse> stopResponseList = stopsOptional.get();

                    Map<Integer, StopResponse> stopResponseMap = createStopResponseMap(stopResponseList);
                    Map<Integer, List<LineJourney>> lineJourneyMap = groupBusLinesByLineNumber(lineDataApiResponse);

                    return processBusStopsForBusLines(stopResponseMap, lineJourneyMap);
                }).join();

        return detailedResult;
    }

    private Map<Integer, StopResponse> createStopResponseMap(List<StopResponse> stopResponseList) {
        return stopResponseList.parallelStream()
                .collect(Collectors.toMap(StopResponse::getId, Function.identity()));
    }

    private Map<Integer, List<LineJourney>> groupBusLinesByLineNumber(
            LineDataApiResponse<LineJourney> lineDataApiResponse) {
        return lineDataApiResponse.getResponseData().getResult().parallelStream()
                .filter(lineJourney -> lineJourney.getDirectionCode() == 1)
                .collect(Collectors.groupingBy(LineJourney::getLineNumber));
    }

    private List<BusLine> processBusStopsForBusLines(Map<Integer, StopResponse> stopResponseMap,
            Map<Integer, List<LineJourney>> lineJourneyMap) {
        return lineJourneyMap.entrySet().parallelStream()
                .map(entry -> {
                    int lineNumber = entry.getKey();
                    List<LineJourney> journeys = entry.getValue();

                    List<BusStop> busStops = journeys.parallelStream()
                            .map(journey -> {
                                StopResponse stopResponse = stopResponseMap.get(journey.getJourneyPatternPointNumber());
                                return stopResponse != null ?
                                        new BusStop(stopResponse.getId(), stopResponse.getGid(),
                                                stopResponse.getPattern_point_gid(),
                                                stopResponse.getSname(), stopResponse.getName()) :
                                        null;
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    int stopCount = busStops.size();
                    return new BusLine(lineNumber, stopCount, busStops);
                })
                .collect(Collectors.toList());
    }


}