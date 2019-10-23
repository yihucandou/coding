package com.yhcd.coding.ssjwt.security;


import com.yhcd.coding.ssjwt.utils.JwtUtils;
import com.yhcd.coding.ssjwt.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;


    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, RedisUtils redisUtils) {
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(TOKEN_HEADER);
        if (StringUtils.isNotEmpty(header) && header.startsWith(TOKEN_PREFIX)) {
            try {
                String token = header.substring(TOKEN_PREFIX.length());
                Claims claims = jwtUtils.parseToken(token);
                String key = claims.getId();
                JwtUserDetails userDetails = redisUtils.get(RedisUtils.Type.USER_AUTHENTICATION, key, JwtUserDetails.class);
                if (userDetails != null && token.equals(userDetails.getToken())) {
                    redisUtils.setObject(RedisUtils.Type.USER_AUTHENTICATION, key, userDetails);
                    JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                    jwtAuthenticationToken.setDetails(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
                }
            } catch (Exception ex) {
                LOGGER.warn(ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
