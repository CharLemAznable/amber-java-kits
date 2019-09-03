package com.github.charlemaznable.amber.forceLogin;

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
@MinerScan
public class ForceLoginConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("AMBER", "forceLogin",
                "appId=1000\n" +
                        "encryptKey=A916EFFC3121F935\n" +
                        "cookieName=cookie-name\n" +
                        "amberLoginUrl=amber-login-url\n" +
                        "localUrl=local-url\n" +
                        "forceLogin=false");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
