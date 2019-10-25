package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.core.spring.ComplexComponentScan;
import com.github.charlemaznable.core.spring.ComplexImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@ComplexImport
@ComplexComponentScan
public class AmberConfigurer implements WebMvcConfigurer {

    @Autowired
    private AmberInterceptor amberInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(amberInterceptor);
    }
}

