package com.jwl.controller;

import com.jwl.pojo.RestOut;
import com.jwl.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author wenlo
 */
@RestController
@RequestMapping("api/call")
public class ApiController {

//    @Autowired
//    RestTemplateBuilder restTemplateBuilder;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/detail/v1/{id}")
    public RestOut<User> getUserById(@PathVariable("id") Long id) {
//        RestTemplate restTemplate = restTemplateBuilder.build();
//        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
//        interceptors.add(new LoadBalancerInterceptor(new RibbonLoadBalancerClient(new SpringClientFactory())));
//        restTemplate.setInterceptors(interceptors);
        User user = restTemplate
                .getForObject("http://uaa-provider/uaa-provider/api/user/detail/v1/" + id, User.class);
        return RestOut.success(user).setRespMsg("操作成功");
    }

}
