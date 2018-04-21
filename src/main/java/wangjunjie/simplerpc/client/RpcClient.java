package wangjunjie.simplerpc.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RpcClient {
    private final InetSocketAddress addr;

    private RpcClient(InetSocketAddress addr) {
        this.addr = addr;
    }

    public RpcClient(String hostname, int port) {
        this(new InetSocketAddress(hostname, port));
    }


    public Object rpcCall(String interfaceName,
                          String methodName,
                          Class<?>[] paramTypes,
                          Object[] args) {


        Socket client = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            client = new Socket();
            client.connect(addr);
            out = new ObjectOutputStream(client.getOutputStream());
            out.writeUTF(interfaceName);
            out.writeUTF(methodName);
            out.writeObject(paramTypes);
            out.writeObject(args);

            in = new ObjectInputStream(client.getInputStream());
            return in.readObject();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

        }


    }
}
