package com.gzlh.task;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.gzlh.cache.CacheManager;
import com.gzlh.utils.AjaxJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WhiteIpTask {
    @Value("${application.name}")
    private String sysName;

    @Value("${main.server}")
    private String mainServerUrl;

//    @Scheduled(fixedRate = 5000)
//    @Async
//    public void getWhiteIp() {
//        try {
//            String resp = HttpUtil.createGet(mainServerUrl + "/api/getWhiteIp?sysName="+sysName).executeAsync().body();
//            WhiteIpDTO dto = JSONUtil.toBean(resp, WhiteIpDTO.class);
//            CacheManager.setWhiteIpDTO(dto);
//        } catch (Exception e) {
//            log.info("get white ip error:{}",e.getMessage());
//        }
//
//
//    }

}
