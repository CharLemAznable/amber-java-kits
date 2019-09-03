package com.github.charlemaznable.amber.interfaceConfig;

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
public class InterfaceConfiguration {

    @PostConstruct
    public static void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("AMBER", "test",
                "appId=1000\n" +
                        "encryptKey=A916EFFC3121F935\n" +
                        "cookieName=cookie-name\n" +
                        "amberLoginUrl=amber-login-url\n" +
                        "localUrl=local-url");
    }

    @PreDestroy
    public static void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
