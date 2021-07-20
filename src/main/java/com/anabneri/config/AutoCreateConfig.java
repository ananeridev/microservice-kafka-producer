package com.anabneri.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateConfig {

    // creating the config and adding the admin properties on application.yml
    // this is not recommened for production enviroment
    @Bean
    public NewTopic booksStoreEvents() {

     return TopicBuilder.name("bookstore-events")
            .partitions(3)
            .replicas(3)
            .build();
    }
}
