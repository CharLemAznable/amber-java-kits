package com.github.charlemaznable.amber.interfaceconfig;

import com.github.charlemaznable.amber.spring.AmberImport;
import com.github.charlemaznable.configservice.diamond.DiamondScan;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan
@AmberImport
@DiamondScan
public class InterfaceConfiguration {

    @PostConstruct
    public static void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("Amber", "InterfaceConfig", """
                InterfaceAppId=InterfaceConfig
                EncryptKey=A916EFFC3121F935
                CookieName=cookie-name
                AmberLoginUrl=amber-login-url
                LocalUrl=local-url""");
    }

    @PreDestroy
    public static void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
