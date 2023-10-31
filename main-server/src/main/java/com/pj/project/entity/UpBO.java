package com.pj.project.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class UpBO implements Serializable {
    private String ip;
    private String ua;
    private String language;
    private String fingerprint;
}
