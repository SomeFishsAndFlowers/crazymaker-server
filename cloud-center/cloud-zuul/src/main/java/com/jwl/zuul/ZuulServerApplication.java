package com.jwl.zuul;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author jiwenlong
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
@EnableHystrix
@EnableDiscoveryClient
@EnableZuulProxy
@EnableCircuitBreaker
@EnableWebSecurity
public class ZuulServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }

}
