package com.literatura.literatura.config;

import com.literatura.literatura.controller.Menu;
import com.literatura.literatura.service.LibroService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Menu menu(LibroService libroService) {
        return new Menu(libroService);
    }
}
