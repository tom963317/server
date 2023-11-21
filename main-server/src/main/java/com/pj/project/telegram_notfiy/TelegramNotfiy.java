package com.pj.project.telegram_notfiy;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("telegram_notfiy")
public class TelegramNotfiy {
    private Long id;


    private String token;


    private String chatid;


    private Integer visitflag;


    private Integer saveflag;



    private Date creattime;



}
