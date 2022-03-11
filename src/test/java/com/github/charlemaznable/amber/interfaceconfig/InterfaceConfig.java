package com.github.charlemaznable.amber.interfaceconfig;

import com.github.charlemaznable.amber.config.AmberConfig;
import com.github.charlemaznable.configservice.diamond.DiamondConfig;

@DiamondConfig(group = "Amber", dataId = "InterfaceConfig")
public interface InterfaceConfig extends AmberConfig {

    @DiamondConfig(dataId = "InterfaceAppId")
    String appId();
}
