package com.github.charlemaznable.amber.anno;

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
public class AnnoConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("Amber", "anno", """
                AppId=anno
                EncryptKey=A916EFFC3121F935
                CookieName=cookie-name
                AmberLoginUrl=amber-login-url
                LocalUrl=local-url""");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
