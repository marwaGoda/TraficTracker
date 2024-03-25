package com.trafik.tracker.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusStop {

    private int id;
    private long gid;
    private long patternPointGid;
    private String sname;
    private String name;

}
