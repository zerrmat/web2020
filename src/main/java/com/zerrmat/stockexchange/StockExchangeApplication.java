package com.zerrmat.stockexchange;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@SpringBootApplication

public class StockExchangeApplication {
	public static void main(String[] args) {
		SpringApplication.run(StockExchangeApplication.class, args);
	}
}
