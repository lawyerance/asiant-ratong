package pers.lyks.asiant.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import pers.lyks.asiant.netty.http.FullHttpResponseInterceptor;

import java.util.List;

/**
 * @author lawyerance
 * @version 1.0 2019-11-23
 */
public class ProxyChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private Channel clientChannel;
    private List<FullHttpResponseInterceptor> interceptors;

    ProxyChannelInitializer(Channel clientChannel, List<FullHttpResponseInterceptor> interceptors) {
        this.clientChannel = clientChannel;
        this.interceptors = interceptors;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpClientCodec());
        ch.pipeline().addLast(new HttpObjectAggregator(6553600));
        ch.pipeline().addLast(new HttpProxyClientHandler(clientChannel, interceptors));
    }

}
