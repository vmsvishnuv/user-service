package com.myproject.user_service.configuration;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${aws.secret.name}")
    private String secretName;

    @Bean
    public DataSource dataSource() throws JsonProcessingException {
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.defaultClient();

        GetSecretValueRequest request = new GetSecretValueRequest()
                .withSecretId(secretName);

        String secret = client.getSecretValue(request).getSecretString();

        // Parse JSON
        JsonNode json = new ObjectMapper().readTree(secret);

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:postgresql://" + json.get("host").asText()
                + ":" + json.get("port").asText()
                + "/" + json.get("dbname").asText());
        ds.setUsername(json.get("username").asText());
        ds.setPassword(json.get("password").asText());

        return ds;
    }
}
