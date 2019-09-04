package com.github.charlemaznable.amber.instanceConfig;

import com.github.charlemaznable.amber.config.AmberConfig;
import org.springframework.stereotype.Component;

@Component
public class InstanceConfig implements AmberConfig {

    @Override
    public String appID() {
        return "1000";
    }

    @Override
    public String encryptKey() {
        return "A916EFFC3121F935";
    }

    @Override
    public String cookieName() {
        return "cookie-name";
    }

    @Override
    public String amberLoginURL() {
        return "amber-login-url";
    }

    @Override
    public String localURL() {
        return "local-url";
    }

    @Override
    public boolean forceLogin() {
        return true;
    }
}
