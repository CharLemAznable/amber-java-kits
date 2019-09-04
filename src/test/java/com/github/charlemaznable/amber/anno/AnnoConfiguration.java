package com.github.charlemaznable.amber.anno;

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
public class AnnoConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("AMBER", "anno",
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
