package com.nikhil.projects.myApp;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName("dev");

        ClientNetworkConfig network = clientConfig.getNetworkConfig();
        network.addAddress("127.0.0.1:5701");

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}