package com.pj.project4sp.admin;

import cn.hutool.core.util.StrUtil;

import com.pj.utils.sg.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pj.project4sp.SP;
import com.pj.project4sp.admin4password.SpAdminPasswordService;

import cn.dev33.satoken.stp.StpUtil;

import javax.annotation.Resource;

/**
 * Service: admin管理员
 *
 * @author kong
 */
@Service
public class SpAdminService {


    @Resource
    SpAdminMapper spAdminMapper;

    @Autowired
    SpAdminPasswordService spAdminPasswordService;



    /**
     * 管理员添加一个管理员
     *
     * @param admin
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public long add(SpAdmin admin) {
        // 检查姓名是否合法
        SpAdminUtil.checkAdmin(admin);

        // 创建人，为当前账号
        admin.setCreateByAid(StpUtil.getLoginIdAsLong());
        // 开始添加
        spAdminMapper.add(admin);
        // 获取主键
        long id = SP.publicMapper.getPrimarykey();
        // 更改密码（md5与明文）
        spAdminPasswordService.updatePassword(id, admin.getPassword2());

        // 返回主键
        return id;
    }


    public AjaxJson addAccount(SpAdmin admin) {
        String name = admin.getName();
        if (StrUtil.isEmpty(name)) {
            return AjaxJson.getError("请输入账号");
        }
        this.add(admin);
        return AjaxJson.getSuccess();
    }

    public void addDou(Long id, int num) {
        spAdminMapper.addDou(id, num);
    }

    public SpAdmin getById(long id) {
        return spAdminMapper.getById(id);
    }

    public void updateCheckNum(SpAdmin spAdmin) {
        spAdminMapper.updateCheckNum(spAdmin);
    }

    public void resetCheckNum(int initCheck) {
        spAdminMapper.resetCheckNum(initCheck);
    }

    public SpAdmin findByOpenid(String openid) {
        return spAdminMapper.findByOpenid(openid);
    }

    public void addDouUserAccount(SpAdmin spAdmin) {
        spAdminMapper.add(spAdmin);
    }

    public void addCheck(String openId, Long checkNum) {
        spAdminMapper.addCheck(openId, checkNum);
    }


}
