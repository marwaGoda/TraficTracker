package com.trafik.tracker.api;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusLine {
    private int id;
    private int stopsCount;
    List<BusStop> stops;

}
