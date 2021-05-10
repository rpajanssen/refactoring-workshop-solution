package com.abnamro.nl.channels.geninfo.bankmail.cache;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;

/**
 * This cache is weakly typed for storing objects, this way it remains flexible to store any object type you want
 * without the need of instantiating a cache for type. The getter is strongly typed using generics so we do not have to
 * implement ugly casts in our code.
 */
@Named
@Singleton
public class Cache {
    HashMap<String, Object> cache = new HashMap<>();

    public void put(String key, Object data) {
        cache.put(key, data);
    }

    public <T> T get(String key) {
        return (T)cache.get(key);
    }
}
