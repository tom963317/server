package com.gzlh.filter;

import cn.hutool.core.util.StrUtil;
import com.gzlh.cache.CacheManager;
import com.gzlh.task.WhiteIpDTO;
import com.gzlh.utils.IpUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
@Order(1)
@WebFilter(initParams = {@WebInitParam(name = "reqFilter",value = "/**")})
@Component
public class ReqFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        WhiteIpDTO dto = CacheManager.getWhiteIpDTO();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String ip = IpUtil.getRealIp(request);
        String ua = request.getHeader("user-agent");
        List<String> ipList = StrUtil.splitTrim(dto.getBlackIpStr(), ",");
        List<String> uaList = StrUtil.splitTrim(dto.getBlackUaStr(), "\n");
        if (!ipList.contains(ip) && !uaList.contains(ua)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
