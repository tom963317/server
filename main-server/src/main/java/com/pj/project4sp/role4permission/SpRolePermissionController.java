package com.pj.project4sp.role4permission;

import java.util.List;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pj.current.satoken.AuthConst;
import com.pj.project4sp.role.SpRoleUtil;
import com.pj.utils.sg.AjaxJson;

import cn.dev33.satoken.stp.StpUtil;

/**
 * Controller: 角色与权限的中间表
 *
 * @author kong
 */
@RestController
@RequestMapping("/SpRolePermission/")
public class SpRolePermissionController {


    /**
     * 底层Service
     */
    @Autowired
    SpRolePermissionService spRolePermissionService;


    /**
     * 拉取权限id列表  根据指定roleId
     *
     * @param roleId
     * @return
     */
    @RequestMapping("getPcodeByRid")
    @SaCheckPermission(value = {AuthConst.ROLE_LIST, AuthConst.KS_ROLE_LIST}, mode = SaMode.OR)
    public AjaxJson getPcodeByRid(@RequestParam(defaultValue = "0") long roleId) {
        // 鉴权
        StpUtil.checkPermission(AuthConst.R1);
        // 防止拉出全部
        if (roleId == 0) {
            return AjaxJson.getError("roleId不能为null或0");
        }
        return AjaxJson.getSuccessData(spRolePermissionService.getPcodeByRid2(roleId + ""));
    }


    /**
     * 拉取菜单id列表  根据当前用户roleId
     */
    @RequestMapping("getPcodeByCurrRid")
    public AjaxJson getPcodeByCurrRid() {
        String roleId = SpRoleUtil.getCurrRoleId();
        List<Object> list = spRolePermissionService.getPcodeByRid2(roleId);
        return AjaxJson.getSuccessData(list);
    }


    /**
     * 修改指定角色的拥有的权限
     *
     * @param roleId 角色id
     * @param code   拥有的权限码集合
     * @return
     */
    @RequestMapping("updatePcodeByRid")
    @SaCheckPermission(value = {AuthConst.ROLE_LIST, AuthConst.KS_ROLE_LIST}, mode = SaMode.OR)
    public AjaxJson updatePcodeByRid(long roleId, String[] code) {
        StpUtil.checkPermission(AuthConst.R1);
        return AjaxJson.getSuccessData(spRolePermissionService.updateRoleMenu(roleId, code));
    }


}
