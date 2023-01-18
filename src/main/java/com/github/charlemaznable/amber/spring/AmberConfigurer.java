package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.amber.config.AmberConfig;
import com.github.charlemaznable.core.spring.ElvesImport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

import java.util.function.Supplier;

import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.spring.SpringContext.getBean;

@Configuration
@ElvesImport
public class AmberConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(amberInterceptor());
    }

    @Bean("com.github.charlemaznable.amber.spring.AmberInterceptor")
    public AmberInterceptor amberInterceptor() {
        return new AmberInterceptor(getBean(AmberConfig.class, defaultAmberConfigSupplier()));
    }

    @Bean("com.github.charlemaznable.amber.spring.AmberCocsHandler")
    public AmberCocsHandler amberCocsHandler() {
        return new AmberCocsHandler(getBean(AmberConfig.class, defaultAmberConfigSupplier()));
    }

    private Supplier<AmberConfig> defaultAmberConfigSupplier() {
        return () -> getConfig(AmberConfig.class);
    }
}


