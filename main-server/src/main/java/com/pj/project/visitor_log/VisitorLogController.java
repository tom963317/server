package com.pj.project.visitor_log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.crypto.digest.DigestUtil;
import com.pj.project.entity.UpBO;
import com.pj.utils.so.SoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.pj.utils.sg.*;
import com.pj.project4sp.SP;

import com.pj.current.satoken.StpUserUtil;
import cn.dev33.satoken.annotation.SaCheckPermission;


/**
 * Controller: visitor_log --
 *
 * @author qzy
 */
@RestController
@RequestMapping("/visitor/")
public class VisitorLogController {

    /**
     * 底层 Service 对象
     */
    @Autowired
    VisitorLogService visitorLogService;


    /**
     * 查集合 - 根据条件（参数为空时代表忽略指定条件）
     */
    @RequestMapping("getList")
    public AjaxJson getList() {
        SoMap so = SoMap.getRequestSoMap();
        List<VisitorLog> list = visitorLogService.getList(so.startPage());
        return AjaxJson.getPageData(so.getDataCount(), list);
    }


    @RequestMapping("up")
    public AjaxJson up(@RequestBody UpBO upBO) {
        visitorLogService.up(upBO);
        return AjaxJson.getSuccess();
    }

    @RequestMapping("statics")
    public AjaxJson statics() {
        long time = System.currentTimeMillis();
        List<VisitorLog> logs = visitorLogService.list();
        long online = logs.stream().filter(visitorLog -> time - visitorLog.getEndTime() < 5000).count();
        long offline=logs.size()-online;
        Map<String,Object>map=new HashMap<>();
        map.put("online",online);
        map.put("offline",offline);
        map.put("total",logs.size());
        return AjaxJson.getSuccessData(map);
    }


}
