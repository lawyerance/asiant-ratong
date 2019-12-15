
package pers.lyks.asiant.autocofigure.krb;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

@EnableConfigurationProperties(value = KerberosProperties.class)
public class JaasKrb5AutoConfiguration {
    @Bean
    public Configuration configuration(KerberosProperties properties) {
        return new KerberosAuthenticationConfiguration(properties);
    }

    static class KerberosAuthenticationConfiguration extends Configuration {
        private KerberosProperties properties;

        KerberosAuthenticationConfiguration(KerberosProperties properties) {
            this.properties = properties;
        }

        @Override
        public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
            return new AppConfigurationEntry[]{properties.buildAppConfigurationEntry()};
        }
    }
}
