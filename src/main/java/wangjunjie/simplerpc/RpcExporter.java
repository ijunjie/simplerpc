package wangjunjie.simplerpc;


import wangjunjie.simplerpc.apis.EchoSerivce;
import wangjunjie.simplerpc.impl.EchoServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ServiceLoader;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RpcExporter {
    private RpcExporter() {
    }

    private static final Executor executor;
    private static final APIProvider provider;

    private static volatile boolean stop = false;

    static {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime()
                .availableProcessors());
        provider = ServiceLoader.load(APIProvider.class, RpcExporter.class.getClassLoader())
                .iterator()
                .next();

        provider.bind(EchoSerivce.class, EchoServiceImpl.class);
    }


    public static void exporter(String hostname, int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(hostname, port));
        try {
            while (!stop) {
                Socket socket = serverSocket.accept();
                executor.execute(new ExporterTask(socket));
            }
        } finally {
            serverSocket.close();
        }
    }

    public static void stopServer() {
        stop = true;
    }

    private static class ExporterTask implements Runnable {
        private final Socket socket;

        ExporterTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream in = null;
            ObjectOutputStream out = null;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                String interfaceName = in.readUTF();
                Class<?> service = Class.forName(interfaceName);
                String methodName = in.readUTF();
                Class<?>[] paramTypes = (Class<?>[]) in.readObject();
                Object[] params = (Object[]) in.readObject();
                Method method = service.getMethod(methodName, paramTypes);
                Object result = method.invoke(provider.get(service), params);

                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
