package com.jwl.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandMetrics;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TaskTimeDemoCommand extends HystrixCommand<String> {

    private final long executeTime;
    private int index;
    private static final AtomicInteger total = new AtomicInteger(0);
    private boolean hasRun = false;


    public TaskTimeDemoCommand(long milliSecond, Setter setter) {
        super(setter);
        this.executeTime = milliSecond;
    }

    @Override
    protected String run() throws Exception {
        hasRun = true;
        index = total.incrementAndGet();
        Thread.sleep(executeTime);
        HystrixCommandMetrics.HealthCounts hc = super.getMetrics().getHealthCounts();
        log.info("succeed - req {}, 熔断器状态{}, 失败率{}%", index, isCircuitBreakerOpen(), hc.getErrorPercentage());
        return "succeed-req " + index;
    }

    @Override
    protected String getFallback() {
        boolean fastFall = !hasRun;
        if(fastFall) {
            index = total.incrementAndGet();
        }
        HystrixCommandMetrics.HealthCounts hc = super.getMetrics().getHealthCounts();
        log.info("failure - req {}, 熔断器状态{}, 失败率{}%", index, isCircuitBreakerOpen(), hc.getErrorPercentage());
        return "failure-req " + index;
    }
}
