package pers.lyks.asiant.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import pers.lyks.asiant.netty.http.FullHttpResponseInterceptor;

import java.util.List;
import java.util.Objects;

/**
 * @author lawyerance
 * @version 1.0 2019-11-23
 */
public class HttpProxyClientHandler extends ChannelInboundHandlerAdapter {

    private Channel clientChannel;
    private List<FullHttpResponseInterceptor> responseInterceptors;

    public HttpProxyClientHandler(Channel clientChannel, List<FullHttpResponseInterceptor> responseInterceptors) {
        this.clientChannel = Objects.requireNonNull(clientChannel, "The http proxy client channel must not be null.");
        this.responseInterceptors = Objects.requireNonNull(responseInterceptors, "The response interceptors must not be null.");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse response = (FullHttpResponse) msg;
        for (FullHttpResponseInterceptor interceptor : responseInterceptors) {
            interceptor.process(response);
        }

        clientChannel.writeAndFlush(msg);
    }

}
