package com.github.charlemaznable.amber.config;

import com.github.charlemaznable.configservice.apollo.ApolloConfig;
import com.github.charlemaznable.configservice.diamond.DiamondConfig;

@ApolloConfig(namespace = "Amber", propertyName = "${amber-config:-default}")
@DiamondConfig(group = "Amber", dataId = "${amber-config:-default}")
public interface AmberConfig {

    @ApolloConfig(propertyName = "AppId")
    @DiamondConfig(dataId = "AppId")
    String appId();

    @ApolloConfig(propertyName = "EncryptKey")
    @DiamondConfig(dataId = "EncryptKey")
    String encryptKey();

    @ApolloConfig(propertyName = "CookieName")
    @DiamondConfig(dataId = "CookieName")
    String cookieName();

    @ApolloConfig(propertyName = "AmberLoginUrl")
    @DiamondConfig(dataId = "AmberLoginUrl")
    String amberLoginUrl();

    @ApolloConfig(propertyName = "LocalUrl")
    @DiamondConfig(dataId = "LocalUrl")
    String localUrl();

    @ApolloConfig(propertyName = "ForceLogin", defaultValue = "TRUE")
    @DiamondConfig(dataId = "ForceLogin", defaultValue = "TRUE")
    boolean forceLogin();
}
