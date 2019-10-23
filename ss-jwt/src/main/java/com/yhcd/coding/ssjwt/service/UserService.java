package com.yhcd.coding.ssjwt.service;

import com.yhcd.coding.ssjwt.model.bean.User;

/**
 * @author hukai
 */
public interface UserService {
    User findByUsername(String username);
}
