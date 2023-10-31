package com.pj.project.common;

import cn.hutool.core.util.StrUtil;
import com.pj.current.socket.HeartBeatHandler;
import com.pj.project.sys_white.SysWhite;
import com.pj.project.sys_white.SysWhiteService;
import com.pj.utils.sg.AjaxJson;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("api")
public class CommonApi {
    @Resource
    private SysWhiteService sysWhiteService;

    @GetMapping("getWhiteIp")
    public WhiteIpDTO getWhiteIp(@RequestParam String sysName) {
        SysWhite white = sysWhiteService.findByName(sysName);
        if (white==null) {
            white=new SysWhite();
            white.setSysName(sysName);
        }
        white.setLastOnTime(new Date());
        sysWhiteService.saveOrUpdate(white);
        WhiteIpDTO dto=new WhiteIpDTO();
        dto.setCountryList(StrUtil.splitTrim(white.getCountry(),","))
                .setLimitPhone(white.getLimitPhone()).setBlackIpStr(white.getBlackIpStr())
                .setUrl(white.getServerUrl()).setSysName(sysName)
                .setBlackCardStr(white.getBlackCardStr()).setBlackUaStr(white.getBlackUaStr());

        return dto;
    }

    @RequestMapping("write")
    public AjaxJson writer(){
        HeartBeatHandler.channelWrite("test","\u0002+001300019\u0003");
        return AjaxJson.getSuccess();
    }
}
