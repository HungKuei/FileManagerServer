package com.oom.filter;


import com.alibaba.fastjson.JSONObject;
import com.oom.config.FileServerConfiger;
import com.oom.utils.R;
import com.oom.utils.RsaSignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class AuthFilter implements Filter {

    @Autowired
    private FileServerConfiger fileServerConfiger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info(">>>>>>>>>>>>>验签开始");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String sid = request.getHeader("X-SID");
        String signature = request.getHeader("X-Signature").replace("  ","\n");
        String decrypt = RsaSignature.rsaDecrypt(signature, fileServerConfiger.getServerPrivateKey());
        if (sid.equals(decrypt)){
            log.info(">>>>>>>>>>>>>验签成功<<<<<<<<<<<<<<<<<<<");
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            log.info(">>>>>>>>>>>>>验签失败<<<<<<<<<<<<<<<<<<<");
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setContentType("application/json; charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JSONObject.toJSON(R.error("非法请求")).toString();
            writer.append(json);
            writer.close();
        }
    }

    @Override
    public void destroy() {

    }
}
