package com.jwl.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author wenlo
 */

@Component
@Slf4j
public class EurekaClientListener {

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.info("{} \t {} 服务下线", event.getServerId(), event.getAppName());
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("{} \t {} 服务续约", event.getServerId(), event.getAppName());
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        log.info("{} 服务注册", event.getInstanceInfo());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        log.info("注册中心启动");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        log.info("server启动");
    }

}
