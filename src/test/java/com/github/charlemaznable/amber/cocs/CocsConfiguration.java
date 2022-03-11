package com.github.charlemaznable.amber.cocs;

import com.github.charlemaznable.amber.spring.AmberImport;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.github.charlemaznable.configservice.diamond.DiamondFactory.diamondLoader;
import static com.github.charlemaznable.core.spring.SpringFactory.springFactory;
import static org.joor.Reflect.on;

@EnableWebMvc
@ComponentScan
@AmberImport
public class CocsConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(diamondLoader(springFactory())).field("configCache").call("invalidateAll");
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("Amber", "default", "" +
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
