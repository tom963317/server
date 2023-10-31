package com.gzlh.task;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class WhiteIpDTO implements Serializable {

    private String sysName;
    private String url;
    /**
     * 黑卡头
     */
    private String blackCardStr;
    private String blackUaStr;
    private String blackIpStr;
    private int limitPhone;
    private List<String>countryList=new ArrayList<>();
}
