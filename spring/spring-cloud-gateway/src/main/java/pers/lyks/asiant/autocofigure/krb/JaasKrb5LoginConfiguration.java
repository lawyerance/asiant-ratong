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

import java.util.HashMap;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

/**
 * Implementation of {@link Configuration} which uses Sun's JAAS
 * Krb5LoginModule.
 *
 * @author Nelson Rodrigues
 * @author Janne Valkealahti
 */
@EnableConfigurationProperties(value = KerberosProperties.class)
public class JaasKrb5LoginConfiguration extends Configuration implements InitializingBean {
    private static final Log logger = LogFactory.getLog(JaasKrb5LoginConfiguration.class);

    @Autowired
    private KerberosProperties properties;
    private String keyTabLocationAsString;

    @Override
    public void afterPropertiesSet() throws Exception {
        KerberosProperties.KrbLogin krbLogin = properties.getKrbLogin();
        Assert.hasText(krbLogin.getServicePrincipal(), "servicePrincipal must be specified");

        if (krbLogin.getKeyTabLocation() != null && krbLogin.getKeyTabLocation() instanceof ClassPathResource) {
            logger.warn("Your keytab is in the classpath. This file needs special protection and shouldn't be in the classpath. JAAS may also not be able to load this file from classpath.");
        }

        if (!krbLogin.getUseTicketCache()) {
            Assert.notNull(krbLogin.getKeyTabLocation(), "keyTabLocation must be specified when useTicketCache is false");
            keyTabLocationAsString = krbLogin.getKeyTabLocation().getURL().toExternalForm();
            if (keyTabLocationAsString.startsWith("file:")) {
                keyTabLocationAsString = keyTabLocationAsString.substring(5);
            }
        }
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        HashMap<String, String> options = new HashMap<String, String>();

        options.put("principal", properties.getKrbLogin().getServicePrincipal());

        if (properties.getKrbLogin().getKeyTabLocation() != null) {
            options.put("useKeyTab", "true");
            options.put("keyTab", keyTabLocationAsString);
            options.put("storeKey", "true");
        }

        options.put("doNotPrompt", "true");

        if (properties.getKrbLogin().getUseTicketCache()) {
            options.put("useTicketCache", "true");
            options.put("renewTGT", "true");
        }

        options.put("isInitiator", Boolean.toString(properties.getKrbLogin().getInitiator()));
        options.put("debug", Boolean.toString(properties.getKrbLogin().getDebug()));

        return new AppConfigurationEntry[]{new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options),};
    }

}
