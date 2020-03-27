package com.github.charlemaznable.amber.interfaceconfig;

import com.github.charlemaznable.amber.config.AmberConfig;
import com.github.charlemaznable.core.miner.MinerConfig;

@MinerConfig(group = "Amber", dataId = "InterfaceConfig")
public interface InterfaceConfig extends AmberConfig {

    @MinerConfig(dataId = "InterfaceAppId")
    String appId();
}
