package com.github.charlemaznable.amber.config;

import com.github.charlemaznable.configservice.Config;

@Config(keyset = "Amber", key = "${amber-config:-default}")
public interface AmberConfig {

    @Config("AppId")
    String appId();

    @Config("EncryptKey")
    String encryptKey();

    @Config("CookieName")
    String cookieName();

    @Config("AmberLoginUrl")
    String amberLoginUrl();

    @Config("LocalUrl")
    String localUrl();

    @Config(key = "ForceLogin", defaultValue = "TRUE")
    boolean forceLogin();
}
