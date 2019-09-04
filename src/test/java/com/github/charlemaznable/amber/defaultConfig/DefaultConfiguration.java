package com.github.charlemaznable.amber.defaultConfig;

import com.github.charlemaznable.amber.spring.AmberImport;
import com.github.charlemaznable.miner.MinerScan;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@EnableWebMvc
@ComponentScan
@AmberImport
@MinerScan("com.github.charlemaznable.amber.config")
public class DefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("AMBER", "default",
                "AppID=1000\n" +
                        "EncryptKey=A916EFFC3121F935\n" +
                        "CookieName=cookie-name\n" +
                        "AmberLoginURL=amber-login-url\n" +
                        "LocalURL=local-url");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
