package com.example.catalog.config.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {

    @Bean
    public CalendarUtilDialect ruCalendarLibDialect() {
        return new CalendarUtilDialect();
    }

}
