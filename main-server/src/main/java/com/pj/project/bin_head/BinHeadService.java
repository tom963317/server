package com.pj.project.bin_head;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pj.project.visitor_log.VisitorLog;
import com.pj.project.visitor_log.VisitorLogMapper;
import com.pj.utils.so.SoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.utils.sg.*;

/**
 * Service: bin_head -- 
 * @author qzy 
 */
@Service
public class BinHeadService extends ServiceImpl<BinHeadMapper, BinHead> implements IService<BinHead> {

	/** 底层 Mapper 对象 */
	@Autowired
	BinHeadMapper binHeadMapper;


	/** 查集合 - 根据条件（参数为空时代表忽略指定条件） */  
	List<BinHead> getList(SoMap so) {
		return binHeadMapper.getList(so);	
	}
	

}
