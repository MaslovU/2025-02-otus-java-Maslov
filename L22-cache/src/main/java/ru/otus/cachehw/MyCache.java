package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private static final Logger log = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    private final int cacheCapacity;

    public MyCache() {
        cacheCapacity = 100;
    }

    public MyCache(int cacheCapacity) {
        this.cacheCapacity = cacheCapacity;
    }

    @Override
    public void put(K key, V value) {
        log.info("Save to cache: key {} value {}", key, value);

        if (cacheCapacity <= cache.size()) {
            cache.clear();
            log.info("clear cache");
        }

        cache.put(key, value);
        notifyListeners(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        notifyListeners(key, value, "REMOVE");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        notifyListeners(key, value, "GET");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(
                l -> {
                    try {
                        l.notify(key, value, action);
                    } catch (Exception e) {
                        log.error("Got listener error: ", e);
                    }
                }
        );
    }
}