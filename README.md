# Simple RPC DEMO

## 简介

这是一个简单的 rpc demo.

服务端公开interface, 客户端根据interface接口的方法签名向服务端发起远程方法调用。

服务端使用SPI机制，绑定一个APIProvider实现。
SPI机制使用类路径下META-INF/services下名称为interface名、内容为实现类名的file声明一个API的实现。

APIProvider用于管理服务端提供的interface和对应的实现类（通过反射可以构建实现类的实例并缓存起来）。

通过APIProvider
- 可以通过bind绑定interface和其对应的实现类的关系
- 可以根据一个interface获取一个实现类的实例



## 测试

```bash
mvn clean test
```