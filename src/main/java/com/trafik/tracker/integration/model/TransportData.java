package com.trafik.tracker.integration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportData {

    private int id;
    private long gid;
    private String name;
    private String designation;
    private String transportMode;
    private String groupOfLines;
    private TransportAuthority transportAuthority;
    private Contractor contractor;
    private Validity valid;
}
