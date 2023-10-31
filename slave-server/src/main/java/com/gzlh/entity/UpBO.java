package com.gzlh.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UpBO implements Serializable {
    private String ip;
    private String ua;
    private String language;
    private String fingerprint;
}
