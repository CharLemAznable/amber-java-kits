package com.github.charlemaznable.amber.config;

import com.github.charlemaznable.miner.MinerConfig;

@MinerConfig(group = "AMBER", dataId = "default")
public interface AmberConfig {

    String appId();

    String encryptKey();

    String cookieName();

    String amberLoginUrl();

    String localUrl();

    @MinerConfig(defaultValue = "TRUE")
    boolean forceLogin();
}
