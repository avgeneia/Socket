package com.mhms.sqlite.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {

	@Value("${spring.datasource.url}")
	private String dbUrl;
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClass;
	
    @Bean
	public DataSource dataSource() {
	        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
	        
	        dataSourceBuilder.driverClassName(driverClass);
	        dataSourceBuilder.url(dbUrl);
	        return dataSourceBuilder.build();   
	}
}
