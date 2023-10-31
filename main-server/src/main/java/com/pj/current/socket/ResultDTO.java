package com.pj.current.socket;

import lombok.Data;

import java.io.Serializable;
@Data
public class ResultDTO implements Serializable {

    private int code;
    private String msg;
    private Object data;


    public ResultDTO() {
    }

    public ResultDTO(int code) {
        this.code = code;
    }

    public ResultDTO(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
