package pers.lyks.asiant;

import pers.lyks.asiant.core.HttpProxyServer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * @author lawyerance
 * @version 1.0 2019-11-23
 */
public class CliApplication {

    public static void main(String[] args) {
        final InetSocketAddress proxy = new InetSocketAddress("localhost", 39200);
        final SocketAddress target = new InetSocketAddress("localhost", 9207);
        HttpProxyServer httpProxyServer = new HttpProxyServer(proxy, target);
        httpProxyServer.addResponseInterceptor(response -> response.headers().add("Proxy-Host", proxy.getAddress().getHostAddress() + (proxy.getPort() == -1 ? "" : ":" + proxy.getPort())));
        httpProxyServer.start();
    }
}
