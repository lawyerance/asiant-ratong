package pers.lyks.asiant.netty.http;

/**
 * HTTP protocol processor is a collection of protocol interceptors that
 * implements the 'Chain of Responsibility' pattern, where each individual
 * protocol interceptor is expected to work on a particular aspect of the HTTP
 * protocol the interceptor is responsible for.
 * <p>
 * Usually the order in which interceptors are executed should not matter as
 * long as they do not depend on a particular state of the execution context.
 * If protocol interceptors have interdependencies and therefore must be
 * executed in a particular order, they should be added to the protocol
 * processor in the same sequence as their expected execution order.
 * <p>
 * Protocol interceptors must be implemented as thread-safe. Similarly to
 * servlets, protocol interceptors should not use instance variables unless
 * access to those variables is synchronized.
 *
 * @author lawyerance
 * @version 1.0 2019-11-30
 */
public interface FullHttpProcessor extends FullHttpRequestInterceptor, FullHttpResponseInterceptor {
}
