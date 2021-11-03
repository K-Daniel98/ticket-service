package com.epam.training.ticketservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;

@Configuration
@ComponentScan
@EnableTransactionManagement
@EnableJpaRepositories
public class ApplicationConfiguration {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Bean
    public DateTimeFormatter dateTimeFormatter() {
        return formatter;
    }

    @Bean
    public PrintStream outputStream() {
        return System.out;
    }

}
