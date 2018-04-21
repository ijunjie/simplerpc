package wangjunjie.simplerpc;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wangjunjie.simplerpc.client.RpcClient;

import java.io.IOException;

public class AppTest {

    @Test
    public void testRpcCall() {
        RpcClient client = new RpcClient("localhost", 8088);
        Object result = client.rpcCall(
                "wangjunjie.simplerpc.apis.EchoSerivce",
                "echo",
                new Class[]{String.class},
                new String[]{"WangJunjie"});
        System.out.println(result);
        RpcExporter.stopServer();
        Assert.assertTrue(result.equals("WangJunjie ---> I am ok."));
    }


    @Before
    public void init() {
        new Thread(() -> {
            try {
                RpcExporter.exporter("localhost", 8088);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @After
    public void post() {
        RpcExporter.stopServer();
    }
}
