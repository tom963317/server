package com.pj.project.tb_card_info;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Model: tb_card_info --
 *
 * @author qzy
 */
@Data
@Accessors(chain = true)
@TableName(TbCardInfo.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class TbCardInfo extends Model<TbCardInfo> implements Serializable {

    // ---------- 模块常量 ----------
    /**
     * 序列化版本id
     */
    private static final long serialVersionUID = 1L;
    /**
     * 此模块对应的表名
     */
    public static final String TABLE_NAME = "tb_card_info";
    /**
     * 此模块对应的权限码
     */
    public static final String PERMISSION_CODE = "tb-card-info";


    // ---------- 表中字段 ----------
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private String carPrefix;
    private String cardType;
    private String debit;
    private String cardLevel;

    /**
     *
     */
    private String info;
    private String remark;

    private int deleteFlag=1;


}
