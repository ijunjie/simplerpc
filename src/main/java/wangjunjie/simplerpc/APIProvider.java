package wangjunjie.simplerpc;

public interface APIProvider {
    void bind(Class<?> api, Class<?> impl);

    <T> T get(Class<T> api);
}
