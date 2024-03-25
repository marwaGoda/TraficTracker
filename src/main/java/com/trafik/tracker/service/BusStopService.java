package com.trafik.tracker.service;

import com.trafik.tracker.api.BusLine;
import java.util.List;

public interface BusStopService {

    List<BusLine> getTop10LinesWithMostStops();

    List<BusLine> getDetailedBusLineWithStops();

}