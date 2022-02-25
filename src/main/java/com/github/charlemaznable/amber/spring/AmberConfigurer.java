package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.core.spring.NeoComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@ElvesImport
@NeoComponentScan
public class AmberConfigurer implements WebMvcConfigurer {

    private final AmberInterceptor amberInterceptor;

    @Autowired
    public AmberConfigurer(AmberInterceptor amberInterceptor) {
        this.amberInterceptor = amberInterceptor;
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(amberInterceptor);
    }
}


