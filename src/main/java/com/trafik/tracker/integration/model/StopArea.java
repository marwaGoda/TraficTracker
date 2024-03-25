package com.trafik.tracker.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopArea {
    private int id;
    private String name;
    private String sname;
    private String type;
}