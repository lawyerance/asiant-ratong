package pers.lyks.asiant.autocofigure.krb;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * @author lawyerance
 * @version 1.0 2019-12-01
 */
@ConfigurationProperties(prefix = "spr")
public class KerberosProperties {
    private final KrbLogin krbLogin = new KrbLogin();

    public KrbLogin getKrbLogin() {
        return krbLogin;
    }

    public static class KrbLogin {

        private String servicePrincipal;
        private Resource keyTabLocation;
        private Boolean useTicketCache = false;
        private Boolean isInitiator = false;
        private Boolean debug = false;

        public void setServicePrincipal(String servicePrincipal) {
            this.servicePrincipal = servicePrincipal;
        }

        public void setKeyTabLocation(Resource keyTabLocation) {
            this.keyTabLocation = keyTabLocation;
        }

        public void setUseTicketCache(Boolean useTicketCache) {
            this.useTicketCache = useTicketCache;
        }

        public void setIsInitiator(Boolean isInitiator) {
            this.isInitiator = isInitiator;
        }

        public void setDebug(Boolean debug) {
            this.debug = debug;
        }

        public String getServicePrincipal() {
            return servicePrincipal;
        }

        public Resource getKeyTabLocation() {
            return keyTabLocation;
        }

        public Boolean getUseTicketCache() {
            return useTicketCache;
        }

        public Boolean getInitiator() {
            return isInitiator;
        }

        public Boolean getDebug() {
            return debug;
        }
    }
}
