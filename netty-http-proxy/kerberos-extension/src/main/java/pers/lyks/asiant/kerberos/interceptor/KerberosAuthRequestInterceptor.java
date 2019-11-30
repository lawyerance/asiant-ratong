package pers.lyks.asiant.kerberos.interceptor;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import org.ietf.jgss.GSSException;
import pers.lyks.asiant.kerberos.krb.SpnegoContext;
import pers.lyks.asiant.netty.http.FullHttpRequestInterceptor;

import java.io.IOException;
import java.security.PrivilegedActionException;
import java.util.Base64;

/**
 * @author lawyerance
 * @version 1.0 2019-11-30
 */
public class KerberosAuthRequestInterceptor implements FullHttpRequestInterceptor {
    private final String nameStr;
    private SpnegoContext context;

    public KerberosAuthRequestInterceptor(String nameStr, SpnegoContext context) {
        this.nameStr = nameStr;
        this.context = context;
    }

    @Override
    public void process(FullHttpRequest request) throws IOException {
        try {
            byte[] bytes = context.createAuthenticateHeader(this.nameStr);
            String token = "Negotiate " + Base64.getEncoder().encodeToString(bytes);
            request.headers().set(HttpHeaderNames.AUTHORIZATION, token);
        } catch (PrivilegedActionException | GSSException e) {
            throw new IOException("create http kerberos authenticate token error", e);
        }
    }
}
