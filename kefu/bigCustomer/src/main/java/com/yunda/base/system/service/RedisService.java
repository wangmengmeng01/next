package com.yunda.base.system.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author "Gu"
 */
public interface RedisService {

    String getStr(String key);

    void putStr(String key, String value, long expire, TimeUnit unit);

    void delStr(String key);

    void putStr(String key, String value);

    //SET
    void putToSet(String key, String... values);

    void deleteAllSet(String key);

    boolean isContainsSetKey(String array, String key);

    void deleteSetSingle(String arraykey, String values);

    Set<String> getSets(String arraykey);

    //Hash
    void putToHash(String key, String hashKey, Object object);

    void deleteAllHash(String key, Object... hashKeys);

    Object getHash(String key, String hashKey);


    void putToHash(String key, Map map);
}
