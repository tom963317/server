package com.pj.project.sys_white;

import java.util.List;

import com.pj.utils.so.SoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.pj.utils.sg.*;
import com.pj.project4sp.SP;

import com.pj.current.satoken.StpUserUtil;
import cn.dev33.satoken.annotation.SaCheckPermission;


/**
 * Controller: sys_white -- 
 * @author qzy 
 */
@RestController
@RequestMapping("/SysWhite/")
public class SysWhiteController {

	/** 底层 Service 对象 */
	@Autowired
	SysWhiteService sysWhiteService;

	/** 增 */  
	@RequestMapping("add")
	@SaCheckPermission(SysWhite.PERMISSION_CODE)
	@Transactional(rollbackFor = Exception.class)
	public AjaxJson add(SysWhite s){
		sysWhiteService.save(s);
		return AjaxJson.getSuccess();
	}

	/** 删 */  
	@RequestMapping("delete")
	@SaCheckPermission(SysWhite.PERMISSION_CODE)
	public AjaxJson delete(Long id){
		sysWhiteService.removeById(id);
		return AjaxJson.getSuccess();
	}

	
	/** 改 */  
	@RequestMapping("update")
	public AjaxJson update(SysWhite s){
		sysWhiteService.updateById(s);
		return AjaxJson.getSuccess();
	}

	/** 查 - 根据id */  
	@RequestMapping("getById")
	public AjaxJson getById(Long id){
		SysWhite s = sysWhiteService.getById(id);
		return AjaxJson.getSuccessData(s);
	}

	/** 查集合 - 根据条件（参数为空时代表忽略指定条件） */  
	@RequestMapping("getList")
	public AjaxJson getList() { 
		SoMap so = SoMap.getRequestSoMap();
		List<SysWhite> list = sysWhiteService.getList(so.startPage());
		list.forEach(sysWhite ->sysWhite.setState( sysWhite.getLastOnTime().getTime()>System.currentTimeMillis()-20000?"在线":"离线"));
		return AjaxJson.getPageData(so.getDataCount(), list);
	}
	
	

	
	
	
	
	

}
