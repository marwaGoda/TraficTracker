package com.trafik.tracker.integration.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinesResponse {

    private List<TransportData> metro;
    private List<TransportData> bus;
    private List<TransportData> tram;
    private List<TransportData> train;
    private List<TransportData> ship;


}
