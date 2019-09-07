package com.github.charlemaznable.amber.defaultConfig;

import com.github.charlemaznable.amber.spring.AmberImport;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@EnableWebMvc
@ComponentScan
@AmberImport
public class DefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("AMBER", "default",
                "AppId=default\n" +
                        "EncryptKey=A916EFFC3121F935\n" +
                        "CookieName=cookie-name\n" +
                        "AmberLoginUrl=amber-login-url\n" +
                        "LocalUrl=local-url");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
