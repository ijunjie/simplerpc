package wangjunjie.simplerpc.impl;


import wangjunjie.simplerpc.apis.EchoSerivce;

public class EchoServiceImpl implements EchoSerivce {
    @Override
    public String echo(String ping) {
        return ping != null ? ping + " ---> I am ok." : " I am ok.";
    }
}
