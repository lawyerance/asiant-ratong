package pers.lyks.asiant.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import pers.lyks.asiant.netty.http.FullHttpRequestInterceptor;
import pers.lyks.asiant.netty.http.FullHttpResponseInterceptor;

import java.net.SocketAddress;
import java.util.List;
import java.util.Objects;


/**
 * @author lawyerance
 * @version 1.0 2019-11-23
 */
public class HttpProxyServerHandler extends ChannelInboundHandlerAdapter {
    private ChannelFuture cf;
    private SocketAddress target;
    private List<FullHttpRequestInterceptor> requestInterceptors;
    private List<FullHttpResponseInterceptor> responseInterceptors;


    public HttpProxyServerHandler(SocketAddress target, List<FullHttpRequestInterceptor> requestInterceptors, List<FullHttpResponseInterceptor> responseInterceptors) {
        this.target = Objects.requireNonNull(target, "The http proxy target socket address must not be null.");
        this.requestInterceptors = Objects.requireNonNull(requestInterceptors, "The request interceptors must not be null.");
        this.responseInterceptors = Objects.requireNonNull(responseInterceptors, "The response interceptors must not be null.");
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            if ("CONNECT".equalsIgnoreCase(request.method().name())) {

                HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                ctx.writeAndFlush(response);
                ctx.pipeline().remove("httpCodec");
                ctx.pipeline().remove("httpObject");
                return;
            }
            // connect target server
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(ctx.channel().eventLoop())
                    .channel(ctx.channel().getClass())
                    .handler(new ProxyChannelInitializer(ctx.channel(), this.responseInterceptors));

            ChannelFuture cf = bootstrap.connect(this.target);

            for (FullHttpRequestInterceptor interceptor : requestInterceptors) {
                interceptor.process(request);
            }
            cf.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    future.channel().writeAndFlush(msg);
                } else {
                    ctx.channel().close();
                }
            });

        } else {
            if (cf == null) {
                // connect target server
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(ctx.channel().eventLoop())
                        .channel(ctx.channel().getClass())
                        .handler(new ChannelInitializer() {

                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx0, Object msg) throws Exception {
                                        ctx.channel().writeAndFlush(msg);
                                    }
                                });
                            }
                        });
                cf = bootstrap.connect(this.target);
                cf.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        future.channel().writeAndFlush(msg);
                    } else {
                        ctx.channel().close();
                    }
                });
            } else {
                cf.channel().writeAndFlush(msg);
            }
        }
    }


}
