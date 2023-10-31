package com.pj.project.sys_white;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pj.project.visitor_log.VisitorLog;
import com.pj.project.visitor_log.VisitorLogMapper;
import com.pj.utils.so.SoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.utils.sg.*;

/**
 * Service: sys_white -- 
 * @author qzy 
 */
@Service
public class SysWhiteService extends ServiceImpl<SysWhiteMapper, SysWhite> implements IService<SysWhite> {

	/** 底层 Mapper 对象 */
	@Autowired
	SysWhiteMapper sysWhiteMapper;



	/** 查集合 - 根据条件（参数为空时代表忽略指定条件） */  
	List<SysWhite> getList(SoMap so) {
		return sysWhiteMapper.getList(so);	
	}


	public SysWhite findByName(String sysName) {
		QueryWrapper<SysWhite>ew=new QueryWrapper<>();
		ew.lambda().eq(SysWhite::getSysName,sysName);
		return getOne(ew);
	}
}
