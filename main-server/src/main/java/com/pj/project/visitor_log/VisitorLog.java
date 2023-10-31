package com.pj.project.visitor_log;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.EqualsAndHashCode;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Model: visitor_log -- 
 * @author qzy 
 */
@Data
@Accessors(chain = true)
@TableName(VisitorLog.TABLE_NAME)
@EqualsAndHashCode(callSuper = false)
public class VisitorLog extends Model<VisitorLog> implements Serializable {

	// ---------- 模块常量 ----------
	/**
	 * 序列化版本id 
	 */
	private static final long serialVersionUID = 1L;	
	/**
	 * 此模块对应的表名 
	 */
	public static final String TABLE_NAME = "visitor_log";	
	/**
	 * 此模块对应的权限码 
	 */
	public static final String PERMISSION_CODE = "visitor-log";	


	// ---------- 表中字段 ----------
	/**
	 *  
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

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
	private Long startTime;	

	/**
	 *  
	 */
	private Long endTime;	

	/**
	 *  
	 */
	private String lang;	

	/**
	 *  
	 */
	private String fingerprint;	

	/**
	 *  
	 */
	private Integer count=0;





	


}
