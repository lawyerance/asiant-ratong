package pers.lyks.asiant.handler;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.util.Arrays;

/**
 * Global request handler, returns the default value
 *
 * @author lawyerance
 * @version 1.0 2019-12-08
 */
public class DispatcherRequestHandler implements HttpRequestHandler {

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        System.out.println(request.getRequestLine());
        System.out.println(Arrays.toString(response.getAllHeaders()));
        response.setEntity(new StringEntity("Welcome to HTTP server built by httpclient"));
    }
}
