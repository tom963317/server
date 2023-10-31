package com.pj.project.bin_head;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Model: bin_head -- 
 * @author qzy 
 */
@Data
@Accessors(chain = true)
@TableName(BinHead.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class BinHead extends Model<BinHead> implements Serializable {

	// ---------- 模块常量 ----------
	/**
	 * 序列化版本id 
	 */
	private static final long serialVersionUID = 1L;	
	/**
	 * 此模块对应的表名 
	 */
	public static final String TABLE_NAME = "bin_head";	
	/**
	 * 此模块对应的权限码 
	 */
	public static final String PERMISSION_CODE = "bin-head";	


	// ---------- 表中字段 ----------
	/**
	 *  
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;	

	/**
	 *  
	 */
	private Integer cardHead;	

	/**
	 *  
	 */
	private String content;	





	


}
