package com.jwl.setter;

import com.netflix.hystrix.*;

public class SetterDemo {

    public static HystrixCommand.Setter setter(String groupKey, String commandKey, String threadPoolKey) {
        HystrixCommandProperties.Setter commandSetter = HystrixCommandProperties.Setter()
                .withCircuitBreakerRequestVolumeThreshold(3)
                .withCircuitBreakerSleepWindowInMilliseconds(5000)
                .withCircuitBreakerErrorThresholdPercentage(60)
                .withExecutionTimeoutEnabled(true)
                .withExecutionTimeoutInMilliseconds(800)
                .withMetricsRollingStatisticalWindowBuckets(10)
                .withMetricsRollingStatisticalWindowInMilliseconds(10000);
        HystrixThreadPoolProperties.Setter threadPoolSetter = HystrixThreadPoolProperties.Setter()
                .withCoreSize(5)
                .withMaximumSize(5);

        HystrixCommandGroupKey hystrixCommandGroupKey = HystrixCommandGroupKey.Factory.asKey(groupKey);
        HystrixCommandKey hystrixCommandKey = HystrixCommandKey.Factory.asKey(commandKey);
        HystrixThreadPoolKey hystrixThreadPoolKey = HystrixThreadPoolKey.Factory.asKey(threadPoolKey);

        return HystrixCommand.Setter
                .withGroupKey(hystrixCommandGroupKey)
                .andCommandKey(hystrixCommandKey)
                .andThreadPoolKey(hystrixThreadPoolKey)
                .andCommandPropertiesDefaults(commandSetter)
                .andThreadPoolPropertiesDefaults(threadPoolSetter);
    }

}
