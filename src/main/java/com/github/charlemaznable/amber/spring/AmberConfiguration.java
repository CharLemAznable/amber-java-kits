package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.core.spring.ComplexBeanNameGenerator;
import com.github.charlemaznable.core.spring.ComplexImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SuppressWarnings("SpringFacetCodeInspection")
@ComponentScan(nameGenerator = ComplexBeanNameGenerator.class)
@Configuration
@ComplexImport
public class AmberConfiguration implements WebMvcConfigurer {

    @Autowired
    private AmberInterceptor amberInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(amberInterceptor).addPathPatterns("/**");
    }
}
