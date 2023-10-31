package com.pj.current.socket;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class JobBO implements Serializable {
    private String phone;
    private String data;
    private int status;
    private int mode;
}
