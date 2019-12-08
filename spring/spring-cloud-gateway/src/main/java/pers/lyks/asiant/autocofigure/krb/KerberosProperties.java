package pers.lyks.asiant.autocofigure.krb;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pers.lyks.kerberos.client.Krb5Property;

import javax.security.auth.login.AppConfigurationEntry;

/**
 * @author lawyerance
 * @version 1.0 2019-12-01
 */
@ConfigurationProperties(prefix = "spring.kerberos")
public class KerberosProperties implements InitializingBean {
    private LoginType loginType = LoginType.CLIENT_CONF;
    private String clientConfig;
    private final LoginModuleProperty loginModule = new LoginModuleProperty();

    @Override
    public void afterPropertiesSet() throws Exception {

    }


    public static class LoginModuleProperty extends Krb5Property {
        private String name = "com.sun.security.auth.module.Krb5LoginModule";
        private ControlFlag controlFlag;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ControlFlag getControlFlag() {
            return controlFlag;
        }

        public void setControlFlag(ControlFlag controlFlag) {
            this.controlFlag = controlFlag;
        }
    }

    public enum LoginType {
        CLIENT_CONF,
        LOGIN_MODULE;
    }

    public enum ControlFlag {
        REQUIRED(AppConfigurationEntry.LoginModuleControlFlag.REQUIRED),
        REQUISITE(AppConfigurationEntry.LoginModuleControlFlag.REQUISITE),
        SUFFICIENT(AppConfigurationEntry.LoginModuleControlFlag.SUFFICIENT),
        OPTIONAL(AppConfigurationEntry.LoginModuleControlFlag.OPTIONAL);

        private final AppConfigurationEntry.LoginModuleControlFlag controlFlag;

        ControlFlag(AppConfigurationEntry.LoginModuleControlFlag controlFlag) {
            this.controlFlag = controlFlag;
        }

        public AppConfigurationEntry.LoginModuleControlFlag getControlFlag() {
            return controlFlag;
        }
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public String getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(String clientConfig) {
        this.clientConfig = clientConfig;
    }

    public LoginModuleProperty getLoginModule() {
        return loginModule;
    }

}
