package com.example.nagoyameshi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * EntityデータをCSVデータへ変換するための初期処理
 * 
 * @author SAMURAI
 * @since 2024-12-25
 */
@Configuration
public class JavaConfig {
	
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
