package com.jwl.command;

import com.jwl.util.HttpRequestUtil;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandMetrics;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class HttpGetCommand extends HystrixCommand<String> {

    private final String url;

    private boolean hasRun = false;

    private int index;
    private static final AtomicInteger total = new AtomicInteger(0);
    private static final AtomicInteger failed = new AtomicInteger(0);

    public HttpGetCommand(String url, Setter setter) {
        super(setter);
        this.url = url;
    }

    @Override
    protected String run() {
        hasRun = true;
        index = total.incrementAndGet();
        log.info("req {} begin...", index);
        Optional<String> result = HttpRequestUtil.simpleGet(url);
        log.info("req {} end, result: {}", index, result.orElse("empty"));
        return "req " + index + ", result: " + result.orElse("empty");
    }

    @Override
    protected String getFallback() {
        boolean isFastFall = !hasRun;
        if(isFastFall) {
            index = total.incrementAndGet();
        }
        if(super.isCircuitBreakerOpen()) {
            HystrixCommandMetrics.HealthCounts hc = super.getMetrics().getHealthCounts();
            log.info("window totalRequests: {}, errorPercentage: {}%", hc.getTotalRequests(), hc.getErrorPercentage());
        }
        boolean isCircuitBreakerOpen = isCircuitBreakerOpen();
        log.info("req {} fallback, 熔断{}, 直接失败{}, 失败次数{}", index, isCircuitBreakerOpen, isFastFall, failed.incrementAndGet());
        return "req " + index + "调用失败";
    }
}
