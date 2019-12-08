package pers.lyks.asiant;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.*;
import pers.lyks.asiant.handler.DispatcherRequestHandler;
import pers.lyks.asiant.handler.ProxyRequestHandler;
import pers.lyks.asiant.server.RequestListenerDaemon;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author lawyerance
 * @version 1.0 2019-12-08
 */
public class HttpClientProxy {
    public static void main(String[] args) throws IOException {

        InetSocketAddress socketAddress = new InetSocketAddress("localhost", 9200);
        int port = 8081;
        // Set up the HTTP protocol processor
        HttpProcessor processor = HttpProcessorBuilder.create()
            .add(new ResponseDate())
            .add(new ResponseContent())
            .add(new ResponseConnControl())
            .build();

        // Set up request handlers
        UriHttpRequestHandlerMapper requestHandlerMapper = new UriHttpRequestHandlerMapper();
        requestHandlerMapper.register("/*", new DispatcherRequestHandler());
//        requestHandlerMapper.register("/*", new ProxyRequestHandler(HttpClientBuilder.create(), socketAddress));

        // Set up the HTTP service
        HttpService httpService = new HttpService(processor, requestHandlerMapper);

        Thread t = new RequestListenerDaemon(port, httpService);
        t.setDaemon(false);
        t.start();
    }
}
