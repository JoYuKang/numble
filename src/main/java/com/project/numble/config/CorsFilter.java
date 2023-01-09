package com.project.numble.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter("/*")
public class CorsFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String origin = request.getHeader("origin");
        response.setHeader("Access-Control-Allow-Origin", findOrigin(origin));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","POST, GET, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "36000");
        response.setHeader("Access-Control-Allow-Headers",
            "Origin, X-Requested-With, Content-Type, Accept, Key, X-Auth-Token, x-auth-token");
        response.setHeader("Access-Control-Expose-Headers",
            "Origin, X-Requested-With, Content-Type, Accept, Key, X-Auth-Token, x-auth-token");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    private String findOrigin(String origin) {
        //return "http://localhost"; //로컬 환경에서 작동 시

        if (origin == null || origin.contains("local")) {
            return "http://localhost:3000";
        }
        return "http://43.201.47.207:3000";
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
