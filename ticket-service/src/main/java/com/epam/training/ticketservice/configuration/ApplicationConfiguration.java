package com.epam.training.ticketservice.configuration;

import com.epam.training.ticketservice.core.pricing.model.BasePrice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableJpaRepositories
public class ApplicationConfiguration {

    @Bean
    public BasePrice basePrice() {
        return new BasePrice(1500);
    }

}
