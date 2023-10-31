package com.pj.project4sp.admin4login;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pj.project4sp.admin.SpAdmin;
import com.pj.project4sp.admin.SpAdminUtil;
import com.pj.project4sp.role4permission.SpRolePermissionService;
import com.pj.project4sp.spcfg.SpCfgUtil;
import com.pj.utils.sg.AjaxJson;
import com.pj.utils.sg.NbUtil;
import com.pj.utils.so.SoMap;

import cn.dev33.satoken.stp.StpUtil;

/**
 * admin账号相关的接口
 *
 * @author kong
 */
@RestController
@RequestMapping("/AccAdmin/")
public class SpAccAdminController {


    @Autowired
    SpAccAdminService spAccAdminService;

    @Autowired
    SpRolePermissionService spRolePermissionService;



    /**
     * 账号、密码登录
     */
    @RequestMapping("doLogin")
    AjaxJson doLogin(String username, String password) {
        // 1、验证参数
        if (NbUtil.isOneNull(username, password)) {
            return AjaxJson.getError("请提供用户名与密码");
        }
        return spAccAdminService.doLogin(username, password);
    }

    /**
     * 账号、密码登录
     */
    @RequestMapping("login")
    AjaxJson login(@RequestBody LoginBO loginBO) {
        return doLogin(loginBO.getUsername(), loginBO.getPassword());
    }

    /**
     * 账号、密码登录
     */
    @RequestMapping("info")
    AjaxJson info() {
        SpAdmin spAdmin = spAccAdminService.getById(StpUtil.getLoginIdAsLong());
        return AjaxJson.getSuccessData(spAdmin);
    }


    /**
     * 退出登录
     */
    @RequestMapping("doExit")
    AjaxJson doExit() {
        StpUtil.logout();
        return AjaxJson.getSuccess();
    }

    /**
     * 退出登录
     */
    @GetMapping("currentLoginUser")
    AjaxJson getCurrentLoginUser() {
        long id = StpUtil.getLoginIdAsLong();
        SpAdmin spAdmin = spAccAdminService.getById(id);
        return AjaxJson.getSuccessData(spAdmin);
    }


    /**
     * 管理员登录后台时需要返回的信息
     */
    @RequestMapping("fristOpenAdmin")
    AjaxJson fristOpenAdmin(HttpServletRequest request) {
        // 当前admin
        SpAdmin admin = SpAdminUtil.getCurrAdmin();

        // 组织参数 (admin信息，权限信息，配置信息)
        SoMap map = new SoMap();
        map.set("admin", SpAdminUtil.getCurrAdmin());
        map.set("per_list", spRolePermissionService.getPcodeByRid2(admin.getRoleId()));
        map.set("app_cfg", SpCfgUtil.getAppCfg());
        return AjaxJson.getSuccessData(map);
    }


}
