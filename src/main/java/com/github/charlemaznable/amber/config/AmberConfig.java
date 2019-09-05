package com.github.charlemaznable.amber.config;

import com.github.charlemaznable.miner.MinerConfig;

@MinerConfig(group = "AMBER", dataId = "default")
public interface AmberConfig {

    @MinerConfig(dataId = "AppId")
    String appId();

    @MinerConfig(dataId = "EncryptKey")
    String encryptKey();

    @MinerConfig(dataId = "CookieName")
    String cookieName();

    @MinerConfig(dataId = "AmberLoginUrl")
    String amberLoginUrl();

    @MinerConfig(dataId = "LocalUrl")
    String localUrl();

    @MinerConfig(dataId = "ForceLogin", defaultValue = "TRUE")
    boolean forceLogin();
}
