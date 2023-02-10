package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.amber.config.AmberConfig;
import com.github.charlemaznable.core.spring.ElvesImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

@Configuration
@ElvesImport
public class AmberConfigurer implements WebMvcConfigurer {

    private final AmberConfig amberConfig;

    @Autowired
    public AmberConfigurer(@Nullable AmberConfig amberConfig) {
        this.amberConfig = amberConfig;
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(amberInterceptor());
    }

    @Bean("com.github.charlemaznable.amber.spring.AmberInterceptor")
    public AmberInterceptor amberInterceptor() {
        return new AmberInterceptor(amberConfig);
    }

    @Bean("com.github.charlemaznable.amber.spring.AmberCocsHandler")
    public AmberCocsHandler amberCocsHandler() {
        return new AmberCocsHandler(amberConfig);
    }
}


