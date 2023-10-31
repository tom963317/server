package com.pj.project4sp.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pj.current.satoken.AuthConst;
import com.pj.project4sp.SP;
import com.pj.project4sp.admin4password.SpAdminPasswordService;
import com.pj.utils.sg.AjaxJson;
import com.pj.utils.so.SoMap;

import cn.dev33.satoken.stp.StpUtil;

import javax.annotation.Resource;

/**
 * Controller -- 系统管理员表
 *
 * @author kong
 */
@RestController
@RequestMapping("/admin/")
public class SpAdminController {

    @Resource
    SpAdminMapper spAdminMapper;

    @Autowired
    SpAdminService spAdminService;

    @Autowired
    SpAdminPasswordService spAdminPasswordService;


    /**
     * 增
     */
    @RequestMapping("add")
    AjaxJson add(SpAdmin admin) {
        StpUtil.checkPermission(AuthConst.ADMIN_LIST);
        long id = spAdminService.add(admin);
        return AjaxJson.getSuccessData(id);
    }

    /**
     * 删
     */
    @RequestMapping("delete")
    AjaxJson delete(@RequestBody SpAdmin spAdmin) {
      Long id=  spAdmin.getId();
        // 不能自己删除自己
        if (StpUtil.getLoginIdAsLong() == id) {
            return AjaxJson.getError("不能自己删除自己");
        }
        int line = spAdminMapper.delete(id);
        return AjaxJson.getByLine(line);
    }

    /**
     * 删 - 根据id列表
     */
    @RequestMapping("deleteByIds")
    AjaxJson deleteByIds() {
        // 鉴权
        // 不能自己删除自己
        List<Long> ids = SoMap.getRequestSoMap().getListByComma("ids", long.class);
        if (ids.contains(StpUtil.getLoginIdAsLong())) {
            return AjaxJson.getError("不能自己删除自己");
        }
        // 开始删除
        int line = SP.publicMapper.deleteByIds("sp_admin", ids);
        return AjaxJson.getByLine(line);
    }

    /**
     * 改  -  name
     */
    @RequestMapping("update")
    AjaxJson update(SpAdmin obj) {
        SpAdminUtil.checkName(obj.getId(), obj.getName());
        int line = spAdminMapper.update(obj);
        return AjaxJson.getByLine(line);
    }


    /**
     * 查
     */
    @RequestMapping("getById")
    AjaxJson getById(long id) {
        StpUtil.checkPermission(AuthConst.ADMIN_LIST);
        Object data = spAdminMapper.getById(id);
        return AjaxJson.getSuccessData(data);
    }

    /**
     * 查
     */
    @RequestMapping("addAccount")
    AjaxJson addAccount(@RequestBody SpAdmin admin) {
        StpUtil.checkLogin();
        admin.setRoleId("1");
        spAdminService.addAccount(admin);
        return AjaxJson.getSuccess();
    }


    /**
     * 查
     */


    /**
     * 查 - 集合
     */
    @RequestMapping("getList")
    AjaxJson getList() {
        SoMap so = SoMap.getRequestSoMap();
        List<SpAdmin> list = spAdminMapper.getList(so.startPage());
        return AjaxJson.getPageData(so.getDataCount(), list);
    }

    @RequestMapping("getByAccountId")
    AjaxJson getByAccountId(String accountId) {
        List<SpAdmin> list = spAdminMapper.list();
        List<SpAdmin> result=new ArrayList<>();
        list.parallelStream().filter(spAdmin -> {
            return StrUtil.splitTrim(spAdmin.getAccountId(), ",").contains(accountId);
        }).findFirst().ifPresent(result::add);
        return AjaxJson.getSuccessData(result);
    }

    /**
     * 改密码
     */
    @RequestMapping("updatePassword")
    AjaxJson updatePassword(long id, String password) {
        StpUtil.checkPermission(AuthConst.ADMIN_LIST);
        int line = spAdminPasswordService.updatePassword(id, password);
        return AjaxJson.getByLine(line);
    }

    /**
     * 改头像
     */
    @RequestMapping("updateAvatar")
    AjaxJson updateAvatar(long id, String avatar) {
        StpUtil.checkPermission(AuthConst.ADMIN_LIST);
        int line = SP.publicMapper.updateColumnById("sp_admin", "avatar", avatar, id);
        return AjaxJson.getByLine(line);
    }

    /**
     * 改状态
     */
    @RequestMapping("updateStatus")
    public AjaxJson updateStatus(long adminId, int status) {
        StpUtil.checkPermission(AuthConst.R1);

        // 验证对方是否为超管(保护超管不受摧残)
        if (StpUtil.hasPermission(adminId, AuthConst.R1)) {
            return AjaxJson.getError("抱歉，对方角色为系统超级管理员，您暂无权限操作");
        }

        // 修改状态
        SP.publicMapper.updateColumnById("sp_admin", "status", status, adminId);
        // 如果是禁用，就停掉其秘钥有效果性，使其账号的登录状态立即无效
        if (status == 2) {
            StpUtil.logout(adminId);
        }
        return AjaxJson.getSuccess();
    }

    /**
     * 改角色
     */
    @RequestMapping("updateRole")
    AjaxJson updateRole(long id, String roleId) {
        StpUtil.checkPermission(AuthConst.R1);
        int line = SP.publicMapper.updateColumnById("sp_admin", "role_id", roleId, id);
        return AjaxJson.getByLine(line);
    }

    /**
     * 返回当前admin信息
     */
    @RequestMapping("getByCurr")
    AjaxJson getByCurr() {
        SpAdmin admin = SpAdminUtil.getCurrAdmin();
        return AjaxJson.getSuccessData(admin);
    }

    /**
     * 当前admin修改信息
     */
    @RequestMapping("updateInfo")
    AjaxJson updateInfo(SpAdmin obj) {
        obj.setId(StpUtil.getLoginIdAsLong());
        SpAdminUtil.checkName(obj.getId(), obj.getName());
        int line = spAdminMapper.update(obj);
        return AjaxJson.getByLine(line);
    }

    @RequestMapping("getAccountAdminList")
    AjaxJson getAccountAdminList() {
        return AjaxJson.getSuccessData(spAdminMapper.getAccountAdminList());
    }

}
