package com.airing.rpc.v3.proxy;

import com.airing.rpc.v3.rpc.Dispatcher;
import com.airing.rpc.v3.rpc.protocol.Body;
import com.airing.rpc.v3.rpc.transport.ClientFactory;
import com.airing.rpc.v3.service.RpcService;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyFactory {

    public static <T> T getInstance(Class<T> interfaceInfo) {
        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] interfaces = {interfaceInfo};
        /**
         * <p>服务提供者可能在远程主机上，也可能与服务消费者在同一个JVM内
         * ，所以由dispatcher决定是走远程调用还是本地调用
         */
        Dispatcher dispatcher = Dispatcher.getInstance();

        return (T) Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args) -> {

            String serviceName = interfaceInfo.getName();
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();

            Object service = dispatcher.get(serviceName);
            Object result = null;

            if (service == null) {
                // 走rpc调用

                /**
                 * <p>content是service的具体数据，而具体的协议在header中控制
                 */
                Body body = new Body();
                body.setServiceName(serviceName);
                body.setMethodName(methodName);
                body.setParameterTypes(parameterTypes);
                body.setArgs(args);

                /**
                 * <ul>
                 *     <li>缺失了服务的注册发现
                 *     <li>第一层负载面向provider
                 *     <li>第二层负载面向连接数
                 * </ul>
                 *
                 * serviceA
                 *      ipA:port
                 *          socket1
                 *          socket2
                 *      ipB:port
                 */


                // 未来返回，回调
                CompletableFuture<Object> completableFuture = ClientFactory.getInstance().transport(body);
                result = String.valueOf(completableFuture.get());
            } else {
                Class<?> clazz = service.getClass();
                Method m = clazz.getMethod(methodName, parameterTypes);
                result = m.invoke(service, args);
            }

            return result;
        });
    }

    public static void main(String[] args) {
        int size = 10;
        AtomicInteger index = new AtomicInteger(0);
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                RpcService rpcService = ProxyFactory.getInstance(RpcService.class);
                String arg = "hello" + index.getAndIncrement();
                String ret = rpcService.rpcMethod(arg);
                System.out.println("req arg: " + arg + ", ret: " + ret);
            });
        }

        for (int i = 0; i < size; i++) {
            threads[i].start();
        }
    }

}
