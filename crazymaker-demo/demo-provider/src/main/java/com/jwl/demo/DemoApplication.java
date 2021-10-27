package com.jwl.demo;

import com.jwl.user.remote.client.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wenlo
 */
@EnableFeignClients(
        clients = UserClient.class
)
@SpringBootApplication(scanBasePackages = { "com.jwl.user.remote", "com.jwl.demo" })
@EnableHystrix  //或@EnableCircuitBreaker
//@ServletComponentScan
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class);
    }
}
