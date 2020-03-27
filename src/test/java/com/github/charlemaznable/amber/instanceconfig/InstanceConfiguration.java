package com.github.charlemaznable.amber.instanceconfig;

import com.github.charlemaznable.amber.spring.AmberImport;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan
@AmberImport
public class InstanceConfiguration {
}
