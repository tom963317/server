package com.pj.project.task;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pj.current.config.SystemObject;
import com.pj.utils.cache.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class TokenTask {


    @Scheduled(cron = "0 0 0 1,20 * ?")
    @Async
    public void token() throws Exception {
        ThreadUtil.execute(new TokenThread());
    }


}
