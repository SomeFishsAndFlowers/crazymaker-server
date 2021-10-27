package com.jwl.demo.user.proporty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wenlo
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class ConfigProperties {

    public ConfigProperties() {
        System.out.println("ConfigProperties init");
    }

    private String finish;

    public void setFinish(String finish) {
        System.out.println("stter: " + finish);
        this.finish = finish;
    }
}
