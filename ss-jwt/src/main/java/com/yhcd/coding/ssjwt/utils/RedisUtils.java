package com.yhcd.coding.ssjwt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author hukai
 */
@Component
public class RedisUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisUtils.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisUtils(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    public enum Type {

        SMS_DIGITS("QIYEGO_MANAGEMENT_SMS_DIGITS_", 5 * 60L),
        USER_AUTHENTICATION("QIYEGO_MANAGEMENT_USER_AUTHENTICATION_", 30 * 60L),
        ;

        private String prefix;
        private Long expire;

        Type(String prefix, Long expire) {
            this.prefix = prefix;
            this.expire = expire;
        }

        public String getPrefix() {
            return prefix;
        }

        public Long getExpire() {
            return expire;
        }

    }

    public void set(Type type, String key, String value) {
        stringRedisTemplate.opsForValue().set(type.getPrefix() + key, value, type.getExpire(), TimeUnit.SECONDS);
    }

    public void setObject(Type type, String key, Object object) {
        try {
            set(type, key, objectMapper.writeValueAsString(object));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public String get(Type type, String key) {
        return stringRedisTemplate.opsForValue().get(type.getPrefix() + key);
    }

    public void delete(Type type, String key) {
        stringRedisTemplate.delete(type.getPrefix() + key);
    }

    public Long getExpire(Type type, String key) {
        return stringRedisTemplate.getExpire(type.getPrefix() + key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(Type type, String key) {
        return stringRedisTemplate.hasKey(type.getPrefix() + key);
    }

    public <T> T get(Type type, String key, Class<T> clazz) {
        try {
            String value = get(type, key);
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return objectMapper.readValue(value, clazz);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }
}
