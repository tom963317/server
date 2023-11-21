package com.pj.project.telegram_notfiy;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pj.utils.cache.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TelegramNotfiyServiceImpl implements TelegramNotfiyService{
    @Autowired
    private TelegramNotfiyMapper mapper;
    @Override
    public TelegramNotfiy getNotify() {
        String notifyStr = RedisUtil.get("TelegramNotify");
        if (notifyStr == null || "".equals(notifyStr)) {
            List<TelegramNotfiy> list = mapper.selectList(new QueryWrapper<>());
            if (list != null && list.size() > 0) {
                TelegramNotfiy telegramNotfiy = list.get(0);
                RedisUtil.set("TelegramNotify", JSON.toJSONString(telegramNotfiy));
                return telegramNotfiy;
            }
        } else {
            return JSON.parseObject(notifyStr, TelegramNotfiy.class);
        }
        return null;
    }

    @Override
    public void updateTelegramNotfiy(TelegramNotfiy notify) {
        notify.setCreattime(new Date());
        mapper.updateById(notify);
        RedisUtil.set("TelegramNotify", JSON.toJSONString(notify));
    }
}
