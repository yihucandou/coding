package com.yhcd.coding.ssjwt.controller;

import com.google.common.base.Preconditions;
import com.yhcd.coding.ssjwt.model.Result;
import com.yhcd.coding.ssjwt.model.bean.User;
import com.yhcd.coding.ssjwt.model.request.LoginRequest;
import com.yhcd.coding.ssjwt.security.JwtAuthenticationToken;
import com.yhcd.coding.ssjwt.security.JwtUserDetails;
import com.yhcd.coding.ssjwt.service.UserService;
import com.yhcd.coding.ssjwt.utils.JwtUtils;
import com.yhcd.coding.ssjwt.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author hukai
 */
@RestController
public class BasicController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public BasicController(UserService userService, JwtUtils jwtUtils, RedisUtils redisUtils, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest req) {
        Preconditions.checkNotNull(loginRequest);
        String username = Preconditions.checkNotNull(loginRequest.getUsername());
        String password = Preconditions.checkNotNull(loginRequest.getPassword());
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.failed("账号不存在");
        }

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return Result.failed("密码错误");
        }

        Authentication authentication = authenticationManager.authenticate(new JwtAuthenticationToken(username, null));
        String token = createToken(authentication);
        JwtUserDetails details = (JwtUserDetails) authentication.getDetails();
        return new Result<>(token);
    }

    private String createToken(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getDetails();
        String token = jwtUtils.generateToken(userDetails.getId().toString());
        userDetails.setToken(token);
        redisUtils.setObject(RedisUtils.Type.USER_AUTHENTICATION, userDetails.getId().toString(), userDetails);
        return token;
    }
}
