package pers.lyks.asiant.handler;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;

/**
 * @author lawyerance
 * @version 1.0 2019-12-08
 */
public class ProxyRequestHandler implements HttpRequestHandler {
    private final HttpHost target;
    private HttpClientBuilder httpClientBuilder;

    public ProxyRequestHandler(HttpClientBuilder httpClientBuilder, HttpHost target) {
        this.httpClientBuilder = httpClientBuilder;
        this.target = target;
    }

    public ProxyRequestHandler(HttpClientBuilder httpClientBuilder, InetSocketAddress inetSocketAddress) {
        this(httpClientBuilder, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
    }

    private ProxyRequestHandler(HttpClientBuilder httpClientBuilder, InetAddress address, int port) {
        this(httpClientBuilder, new HttpHost(address, port));
    }


    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        System.out.println("Request line: " + request.getRequestLine() + ", and headers: " + Arrays.toString(request.getAllHeaders()));
        // must remove header 'Content-Length', otherwise an error will be reported like 'Content-Length header already present'
        request.removeHeaders(HttpHeaders.CONTENT_LENGTH);
        request.setHeader("Connection", "close, TE");

        // renew request uri
        String newUri = target.toURI() + request.getRequestLine().getUri();
        RequestBuilder builder = RequestBuilder.copy(request).setUri(newUri);

        HttpCoreContext httpCoreContext = HttpCoreContext.adapt(context);
        httpCoreContext.setTargetHost(this.target);
        EntityUtils.consume(response.getEntity());

        CloseableHttpResponse execute = httpClientBuilder.build().execute(builder.build(), httpCoreContext);
        HttpEntity entity = execute.getEntity();
        System.out.println(Arrays.toString(response.getAllHeaders()));
        response.setEntity(entity);
    }
}
