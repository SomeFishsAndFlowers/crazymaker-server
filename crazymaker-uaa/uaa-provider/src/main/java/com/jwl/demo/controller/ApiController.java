package com.jwl.demo.controller;

import com.jwl.common.result.RestOut;
import com.jwl.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
