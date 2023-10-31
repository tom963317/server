package com.pj.project4sp.global;

import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pj.utils.sg.AjaxError;
import com.pj.utils.sg.AjaxJson;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;

/**
 * 全局异常拦截 
 * <p> @ControllerAdvice 可指定包前缀，例如：(basePackages = "com.pj.controller.admin")
 * @author kong
 *
 */
@ControllerAdvice
@Slf4j
public class GlobalException {

	/** 全局异常拦截  */
	@ResponseBody
	@ExceptionHandler
	public AjaxJson handlerException(Exception e) {

		// 打印堆栈，以供调试
		log.error(e.getMessage());
    	// 记录日志信息
    	AjaxJson aj = null;
		Throwable e2 = e.getCause();
		
		// ------------- 判断异常类型，提供个性化提示信息 
		
    	// 如果是未登录异常 
		if(e instanceof NotLoginException){	
			aj = AjaxJson.getNotLogin();
		} 
		// 如果是权限异常
		else if(e instanceof NotPermissionException) {	
			NotPermissionException ee = (NotPermissionException) e;
			aj = AjaxJson.getNotJur("无此权限：" + ee.getCode());
		} 
		// 如果是AjaxError，则获取其具体code码 
		else if(e instanceof AjaxError) {		
			AjaxError ee = (AjaxError) e;
			aj = AjaxJson.get(ee.getCode(), ee.getMessage());
		}  

		// 如果是redis连接异常 ( 由于redis连接异常，系统已经无法正常工作，所以此处需要立即返回 )
		else if(e instanceof RedisConnectionFailureException) {	
			aj = AjaxJson.getError("Redis异常，请检查连接信息");
			return aj;
		}
		// 普通异常输出：500 + 异常信息 
		else {
			aj = AjaxJson.getError(e.getMessage());
		}

		return aj;
	}

}
