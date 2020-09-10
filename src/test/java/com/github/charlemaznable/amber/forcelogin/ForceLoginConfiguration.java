package com.github.charlemaznable.amber.forcelogin;

import com.github.charlemaznable.amber.spring.AmberImport;
import com.github.charlemaznable.core.miner.MinerScan;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@EnableWebMvc
@ComponentScan
@AmberImport
@MinerScan
public class ForceLoginConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("Amber", "forceLogin", "" +
                "AppId=forceLogin\n" +
                "EncryptKey=A916EFFC3121F935\n" +
                "CookieName=cookie-name\n" +
                "AmberLoginUrl=amber-login-url\n" +
                "LocalUrl=local-url\n" +
                "ForceLogin=false");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
