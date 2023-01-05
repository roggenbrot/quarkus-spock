package io.quarkiverse.test.spock.junit;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SpockExtensionStore implements ExtensionContext.Store {

    private Map<Object, Object> store = new HashMap<>();

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public <V> V get(Object key, Class<V> requiredType) {
        return (V) store.get(key);
    }

    @Override
    public <K, V> Object getOrComputeIfAbsent(K key, Function<K, V> defaultCreator) {
        Object value = store.get(key);
        if (value != null) {
            return value;
        }
        return defaultCreator.apply(key);

    }

    @Override
    public <K, V> V getOrComputeIfAbsent(K key, Function<K, V> defaultCreator, Class<V> requiredType) {
        return (V) getOrComputeIfAbsent(key, defaultCreator);
    }

    @Override
    public void put(Object key, Object value) {
        store.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return store.remove(key);
    }

    @Override
    public <V> V remove(Object key, Class<V> requiredType) {
        return (V) remove(key);
    }
}
