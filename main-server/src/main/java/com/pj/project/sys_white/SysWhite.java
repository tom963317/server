package com.pj.project.sys_white;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Model: sys_white -- 
 * @author qzy 
 */
@Data
@Accessors(chain = true)
@TableName(SysWhite.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class SysWhite extends Model<SysWhite> implements Serializable {

	// ---------- 模块常量 ----------
	/**
	 * 序列化版本id 
	 */
	private static final long serialVersionUID = 1L;	
	/**
	 * 此模块对应的表名 
	 */
	public static final String TABLE_NAME = "sys_white";	
	/**
	 * 此模块对应的权限码 
	 */
	public static final String PERMISSION_CODE = "sys-white";	


	// ---------- 表中字段 ----------
	/**
	 *  
	 */
	@TableId(type = IdType.AUTO)
	private Long id;	

	/**
	 *  
	 */
	private String sysName;	

	/**
	 *  
	 */
	private String serverUrl;	

	/**
	 *  
	 */
	private String country;	
	private String state;
	/**
	 * 黑卡头
	 */
	private String blackCardStr;

	private String blackUaStr;

	private String blackIpStr;

	private Date lastOnTime;

	private int limitPhone=0;





	


}
