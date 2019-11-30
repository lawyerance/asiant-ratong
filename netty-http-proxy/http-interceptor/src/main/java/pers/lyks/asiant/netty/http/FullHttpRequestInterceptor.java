package pers.lyks.asiant.netty.http;

import io.netty.handler.codec.http.FullHttpRequest;

import java.io.IOException;

/**
 * HTTP protocol interceptor is a routine that implements a specific aspect of
 * the HTTP protocol. Usually protocol interceptors are expected to act upon
 * one specific header or a group of related headers of the incoming message
 * or populate the outgoing message with one specific header or a group of
 * related headers.
 * <p>
 * Protocol Interceptors can also manipulate content entities enclosed with messages.
 * Usually this is accomplished by using the 'Decorator' pattern where a wrapper
 * entity class is used to decorate the original entity.
 * <p>
 * Protocol interceptors must be implemented as thread-safe. Similarly to
 * servlets, protocol interceptors should not use instance variables unless
 * access to those variables is synchronized.
 *
 * @author lawyerance
 * @version 1.0 2019-11-30
 */
@FunctionalInterface
public interface FullHttpRequestInterceptor {
    /**
     * Processes a request.
     * On the client side, this step is performed before the request is
     * sent to the server. On the server side, this step is performed
     * on incoming messages before the message body is evaluated.
     *
     * @param request the http full request to pre process
     * @throws IOException in case of an I/O error
     */
    void process(FullHttpRequest request) throws IOException;
}
