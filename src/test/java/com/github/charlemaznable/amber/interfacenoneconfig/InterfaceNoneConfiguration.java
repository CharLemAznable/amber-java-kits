package com.github.charlemaznable.amber.interfacenoneconfig;

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
public class InterfaceNoneConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
