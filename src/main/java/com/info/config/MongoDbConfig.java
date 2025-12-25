package com.info.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.info.repository")
public class MongoDbConfig extends AbstractReactiveMongoConfiguration {

	@Value("${database.name}")
	private String databaseName;

	@Value("${database.host}")
	private String databaseHost;

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Override
	public MongoClient reactiveMongoClient() {
		String name = databaseHost;
		return MongoClients.create(name);
	}

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() {
		return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
	}

}
