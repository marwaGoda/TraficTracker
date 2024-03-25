package com.trafik.tracker.service;

import com.trafik.tracker.api.BusLine;
import java.util.List;

public interface BusStopService {
    public List<BusLine> getTop10LinesWithMostStops();
    public List<BusLine> getDetailedBusLineWithStops();

}