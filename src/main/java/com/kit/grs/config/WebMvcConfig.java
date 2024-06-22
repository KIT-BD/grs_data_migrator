package com.kit.grs.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
public class WebMvcConfig {

    @Value("${elastic.host}")
    private String host;

    @Value("${elastic.port}")
    private int port;

    @Value("${elastic.username}")
    private String username;

    @Value("${elastic.password}")
    private String password;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestHighLevelClient client() {
        RestClientBuilder builder = RestClient.builder(new HttpHost(host,port));

        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                .setDefaultCredentialsProvider(credentialsProvider));
        return new RestHighLevelClient(builder);
    }

    @Bean("consumeProducts")
    public Executor restConsumerPool() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(1);
        exec.setMaxPoolSize(1);
        exec.setQueueCapacity(10);
        exec.setThreadNamePrefix("first-");
        exec.initialize();
        return exec;
    }

    @Bean("consumeOrders")
    public Executor syncRegistrationPool() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(1);
        exec.setMaxPoolSize(1);
        exec.setQueueCapacity(10);
        exec.setThreadNamePrefix("second-");
        exec.initialize();
        return exec;
    }
}

