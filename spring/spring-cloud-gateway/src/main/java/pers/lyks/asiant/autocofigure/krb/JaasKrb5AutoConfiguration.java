/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pers.lyks.asiant.autocofigure.krb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

/**
 * Implementation of {@link Configuration} which uses Sun's JAAS
 * Krb5LoginModule.
 *
 * @author Nelson Rodrigues
 * @author Janne Valkealahti
 */
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
            KerberosProperties.LoginModuleProperty loginModule = properties.getLoginModule();
            return new AppConfigurationEntry[]{new AppConfigurationEntry(loginModule.getName(),
                loginModule.getControlFlag().getControlFlag(), loginModule.buildOptions()),};
        }
    }
}
