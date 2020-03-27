package com.github.charlemaznable.amber.instanceconfig;

import com.github.charlemaznable.amber.config.AmberConfig;
import org.springframework.stereotype.Component;

@Component
public class InstanceConfig implements AmberConfig {

    @Override
    public String appId() {
        return "InstanceConfig";
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
    public String amberLoginUrl() {
        return "amber-login-url";
    }

    @Override
    public String localUrl() {
        return "local-url";
    }

    @Override
    public boolean forceLogin() {
        return true;
    }
}
