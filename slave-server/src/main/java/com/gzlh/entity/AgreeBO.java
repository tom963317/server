package com.gzlh.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AgreeBO implements Serializable {
    private int step;
    private int result;
    private long id;
    private String fingerprint;
    private String path;
}
