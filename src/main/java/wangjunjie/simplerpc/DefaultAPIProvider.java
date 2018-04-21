package wangjunjie.simplerpc;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAPIProvider implements APIProvider {
    // interface class => implement class
    private static final Map<Class<?>, Class<?>> bindings;
    // Cache: interface class => implement instance
    private static final Map<Class<?>, Object> cache;

    static {
        bindings = new ConcurrentHashMap<>();
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public <T> T get(Class<T> api) {
        if (cache.containsKey(api))
            return (T) cache.get(api);

        if (bindings.containsKey(api)) {
            try {
                T impl = (T) bindings.get(api).newInstance();
                cache.put(api, impl);
                return impl;
            } catch (IllegalAccessException e) {
                throw new RuntimeException("IllegalAccessException: " +
                        e.getMessage());
            } catch (InstantiationException e) {
                throw new RuntimeException("Instantiation error for " +
                        api.getName() + ", " + e.getMessage());
            }
        }
        throw new RuntimeException("No implementation for " + api.getName());
    }

    @Override
    public void bind(Class<?> api, Class<?> impl) {
        bindings.put(api, impl);
    }
}
