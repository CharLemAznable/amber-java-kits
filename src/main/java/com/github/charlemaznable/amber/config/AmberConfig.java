package com.github.charlemaznable.amber.config;

import com.github.charlemaznable.miner.MinerConfig;

@MinerConfig(group = "AMBER", dataId = "default")
public interface AmberConfig {

    @MinerConfig(dataId = "AppID")
    String appID();

    @MinerConfig(dataId = "EncryptKey")
    String encryptKey();

    @MinerConfig(dataId = "CookieName")
    String cookieName();

    @MinerConfig(dataId = "AmberLoginURL")
    String amberLoginURL();

    @MinerConfig(dataId = "LocalURL")
    String localURL();

    @MinerConfig(dataId = "ForceLogin", defaultValue = "TRUE")
    boolean forceLogin();
}
