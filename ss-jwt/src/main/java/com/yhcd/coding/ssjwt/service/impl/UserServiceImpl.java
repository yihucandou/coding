package com.yhcd.coding.ssjwt.service.impl;

import com.google.common.collect.Lists;
import com.yhcd.coding.ssjwt.model.bean.User;
import com.yhcd.coding.ssjwt.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author hukai
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findByUsername(String username) {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("123");
        user.setId(1L);
        user.setRoles(Lists.newArrayList("ADMIN"));
        return user;
    }

}
