package pers.lyks.asiant.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.lyks.asiant.netty.http.FullHttpRequestInterceptor;
import pers.lyks.asiant.netty.http.FullHttpResponseInterceptor;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lawyerance
 * @version 1.0 2019-11-23
 */
public class HttpProxyServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpProxyServer.class);
    private final SocketAddress proxyAddress;
    private final SocketAddress targetAddress;
    private final List<FullHttpRequestInterceptor> requestInterceptors = new ArrayList<>();
    private final List<FullHttpResponseInterceptor> responseInterceptors = new ArrayList<>();

    public HttpProxyServer(SocketAddress proxy, SocketAddress target) {
        this.proxyAddress = proxy;
        this.targetAddress = target;
    }

    public void start() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast("httpCodec", new HttpServerCodec());
                            ch.pipeline().addLast("httpObject", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("serverHandle", new HttpProxyServerHandler(targetAddress, requestInterceptors, responseInterceptors));
                        }
                    });
            ChannelFuture f = b.bind(proxyAddress).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {


        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void addRequestInterceptor(FullHttpRequestInterceptor requestInterceptor) {
        this.requestInterceptors.add(requestInterceptor);
    }

    public void addResponseInterceptor(FullHttpResponseInterceptor responseInterceptor) {
        this.responseInterceptors.add(responseInterceptor);

    }
}
