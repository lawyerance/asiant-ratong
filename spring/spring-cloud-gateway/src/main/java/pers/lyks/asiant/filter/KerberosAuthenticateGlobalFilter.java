package pers.lyks.asiant.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.security.auth.login.Configuration;

/**
 * @author lawyerance
 * @version 1.0 2019-12-01
 */
@Component
public class KerberosAuthenticateGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(KerberosAuthenticateGlobalFilter.class);

    @Resource
    private Configuration configuration;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        logger.info("Request uri {} with headers: {}", exchange.getRequest().getRemoteAddress(), headers);
        //向headers中放文件，记得build
        ServerHttpRequest host = exchange.getRequest().mutate().header("X-User", "guanguan").build();
        //将现在的request 变成 change对象
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
    }

    @Override
    public int getOrder() {
        //Priority can be low, just add it before the request
        return 99;
    }
}
