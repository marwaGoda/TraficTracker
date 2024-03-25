package com.trafik.tracker.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopResponse {
    private int id;
    private long gid;
    private long pattern_point_gid;
    private String name;
    private String sname;
    private String designation;
    private int local_num;
    private String type;
    private boolean has_entrance;
    private double lat;
    private double lon;
    private double door_orientation;
    private TransportAuthority transport_authority;
    private StopArea stop_area;
    private Validity valid;

    public StopResponse(int id, long gid, long pattern,  String name, String sname) {
        this.id = id;
        this.gid = gid;
        this.pattern_point_gid = pattern;
        this.name = name;
        this.sname = sname;
    }
}