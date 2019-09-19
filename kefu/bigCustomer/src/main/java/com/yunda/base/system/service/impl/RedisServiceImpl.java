package com.yunda.base.system.service.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.yunda.base.system.service.RedisService;



/**
 * @author Gu
 */
@Service
public class RedisServiceImpl implements RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisServiceImpl(StringRedisTemplate stringRedisTemplate, RedisTemplate<String, String> redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void putStr(String key, String value, long expire, TimeUnit unit) {
        ValueOperations<String, String> redis = stringRedisTemplate.opsForValue();
        redis.set(key, value, expire, unit);
    }

    @Override
    public void putStr(String key, String value) {
        ValueOperations<String, String> redis = stringRedisTemplate.opsForValue();
        redis.set(key, value);
    }

    @Override
    public String getStr(String key) {
        ValueOperations<String, String> redis = stringRedisTemplate.opsForValue();
        return redis.get(key);
    }

    @Override
    public void delStr(String key) {
        ValueOperations<String, String> valueOper = stringRedisTemplate.opsForValue();
        RedisOperations<String, String> redisOperations = valueOper.getOperations();
        redisOperations.delete(key);
    }

    @Override
    public void putToSet(String key, String... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public void deleteAllSet(String key) {
        redisTemplate.opsForSet().getOperations().delete(key);
    }

    @Override
    public boolean isContainsSetKey(String array, String key) {
        return redisTemplate.opsForSet().isMember(array, key);
    }

    @Override
    public void deleteSetSingle(String arraykey, String values) {
        redisTemplate.opsForSet().remove(arraykey, (Object) values);
    }

    @Override
    public Set<String> getSets(String arraykey) {
        return redisTemplate.opsForSet().members(arraykey);
    }


    @Override
    public void putToHash(String key, String hashKey, Object obj) {
        redisTemplate.opsForHash().put(key, hashKey, obj);
    }

    @Override
    public void deleteAllHash(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    @Override
    public Object getHash(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public void putToHash(String key, Map map) {
        redisTemplate.opsForHash().putAll(key, map);
    }
}