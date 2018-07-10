package com.aaron.eureka.client1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * Hello world!
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ApplicationClient1 {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationClient1.class,args);
    }
}