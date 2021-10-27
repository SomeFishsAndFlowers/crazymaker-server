package com.jwl.demo.config;

import com.jwl.demo.filter.TestFilter;
import org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;

/**
 * @author jiwenlong
 */
//@Configuration
public class FilterConfiguration {

    @Bean
    public Filter filter() {
        return new TestFilter();
    }


//    @Bean
//    public FilterRegistrationBean<Filter> filterRegistrationBean() {
//        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new DelegatingFilterProxy("filter"));
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setOrder(1);
//        registrationBean.setName("filter");
//        return registrationBean;
//    }

    @Bean
    public DelegatingFilterProxyRegistrationBean filterDelegatingFilterProxyRegistrationBean() {
        DelegatingFilterProxyRegistrationBean delegatingFilterProxyRegistrationBean
                = new DelegatingFilterProxyRegistrationBean("filter");
        delegatingFilterProxyRegistrationBean.addUrlPatterns("/*");
        return delegatingFilterProxyRegistrationBean;
    }
}
