package com.github.charlemaznable.amber.interfaceNoneConfig;

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
