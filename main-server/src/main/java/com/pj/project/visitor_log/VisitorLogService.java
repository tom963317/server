package com.pj.project.visitor_log;

import java.util.List;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pj.project.bin_card.BinCard;
import com.pj.project.bin_card.BinCardMapper;
import com.pj.project.entity.UpBO;
import com.pj.utils.TelegramBotUtil;
import com.pj.utils.so.SoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.pj.utils.sg.*;

/**
 * Service: visitor_log --
 *
 * @author qzy
 */
@Service
public class VisitorLogService extends ServiceImpl<VisitorLogMapper, VisitorLog> implements IService<VisitorLog> {

    /**
     * 底层 Mapper 对象
     */
    @Autowired
    VisitorLogMapper visitorLogMapper;

    @Autowired
    private BinCardMapper binCardMapper;


    /**
     * 查集合 - 根据条件（参数为空时代表忽略指定条件）
     */
    List<VisitorLog> getList(SoMap so) {
        return visitorLogMapper.getList(so);
    }


    public VisitorLog findByFingerprint(String fingerprint) {
        QueryWrapper<VisitorLog> ew = new QueryWrapper<>();
        ew.lambda().eq(VisitorLog::getFingerprint, fingerprint);
        List<VisitorLog> list = this.list(ew);
        return list.isEmpty() ? null : list.get(0);
    }

    @Async
    public void up(UpBO upBO) {
        String fingerprint = DigestUtil.md5Hex(upBO.getIp() + upBO.getUa() + upBO.getLanguage());
        VisitorLog log = findByFingerprint(fingerprint);
        long time = System.currentTimeMillis();
        if (log == null) {
            log = new VisitorLog();
            log.setStartTime(time);
            //第一次进入发送提示消息
            TelegramBotUtil bot = new TelegramBotUtil();
            QueryWrapper<BinCard> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("fingerprint", fingerprint);
            BinCard binCard = binCardMapper.selectOne(queryWrapper);
            if (binCard != null) {
                String message = binCard.getId() + "号新用户浏览";
                bot.sendMessageToGroup(message);
            }
        }
        log.setCount(log.getCount() + 1)
                .setIp(upBO.getIp())
                .setFingerprint(fingerprint)
                .setUa(upBO.getUa())
                .setLang(upBO.getLanguage())
                .setEndTime(time);
        this.saveOrUpdate(log);

    }
}
