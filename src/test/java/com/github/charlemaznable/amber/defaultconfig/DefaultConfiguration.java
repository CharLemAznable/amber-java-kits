package com.github.charlemaznable.amber.defaultconfig;

import com.github.charlemaznable.amber.spring.AmberImport;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.github.charlemaznable.core.miner.MinerFactory.springMinerLoader;
import static org.joor.Reflect.on;

@EnableWebMvc
@ComponentScan
@AmberImport
public class DefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        on(springMinerLoader()).field("minerCache").call("invalidateAll");
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
