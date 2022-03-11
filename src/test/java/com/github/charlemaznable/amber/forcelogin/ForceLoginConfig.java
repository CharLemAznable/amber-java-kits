package com.github.charlemaznable.amber.forcelogin;

import com.github.charlemaznable.amber.config.AmberConfig;
import com.github.charlemaznable.configservice.diamond.DiamondConfig;

@DiamondConfig(group = "Amber", dataId = "forceLogin")
public interface ForceLoginConfig extends AmberConfig {
}
