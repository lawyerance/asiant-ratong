package pers.lyks.asiant.kerberos.krb;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * @author lawyerance
 * @version 1.0 2019-11-27
 */
public final class SpnegoContext {
    private final LoginContext context;

    public SpnegoContext(LoginContext context) {
        this.context = context;
    }

    public GSSContext getContext(String nameStr) throws GSSException, PrivilegedActionException {
        GSSName gssName = SpnegoProvider.GSS_MANAGER.createName(nameStr, GSSName.NT_HOSTBASED_SERVICE, SpnegoProvider.SPNEGO_OID);
        return Subject.doAs(context.getSubject(), (PrivilegedExceptionAction<GSSContext>) () -> {
            GSSCredential credential = SpnegoProvider.GSS_MANAGER.createCredential(null
                    , GSSCredential.DEFAULT_LIFETIME
                    , SpnegoProvider.SUPPORTED_OIDS
                    , GSSCredential.INITIATE_ONLY);
            GSSContext context = SpnegoProvider.GSS_MANAGER.createContext(gssName
                    , SpnegoProvider.SPNEGO_OID
                    , credential
                    , GSSContext.DEFAULT_LIFETIME);

            context.requestMutualAuth(true);
            context.requestConf(true);
            context.requestInteg(true);
            context.requestReplayDet(true);
            context.requestSequenceDet(true);
            return context;
        });
    }

    public byte[] createAuthenticateHeader(String nameStr) throws PrivilegedActionException, GSSException {
        GSSContext ctx = getContext(nameStr);
        return Subject.doAs(context.getSubject(), (PrivilegedExceptionAction<byte[]>) () -> ctx.initSecContext(new byte[0], 0, 0));
    }
}
