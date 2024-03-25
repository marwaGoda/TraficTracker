package com.trafik.tracker.controller;

import com.trafik.tracker.api.BusLine;
import com.trafik.tracker.service.BusStopService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bus")
public class LinesStopsController {

    private final BusStopService busStopService;

    @Autowired
    public LinesStopsController(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    @GetMapping("/top10lines")
    public List<BusLine> getTop10LinesWithMostStops() {
        return busStopService.getTop10LinesWithMostStops();
    }

    @GetMapping("/detailedLines")
    public List<BusLine> getDetailedTop10LinesWithMostStops() {
        return busStopService.getDetailedBusLineWithStops();
    }
}

