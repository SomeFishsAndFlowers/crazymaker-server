package com.jwl;

import com.jwl.command.HttpGetCommand;
import com.jwl.command.TaskTimeDemoCommand;
import com.jwl.setter.SetterDemo;
import com.jwl.util.HttpRequestUtil;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class HystricTest {

    private final  HystrixCommand.Setter setter =
            SetterDemo.setter("group-1", "command-1", "threadPool-1");


    @Test
    public void test() {
        Optional<String> res = HttpRequestUtil.simpleGet("https://www.baidu.com");
        log.info("res: {}", res.orElse("empty"));
    }

    @Test
    public void test1() {
        HttpGetCommand command = new HttpGetCommand("https://www.baidu.com", setter);
        String execute = command.execute();
        log.info("result: {}", execute);
    }

    @Test
    public void testExecute() {
        for (int i = 0; i < 5; i++) {
            HttpGetCommand command = new HttpGetCommand("http://localhost:8000/demo-provider/demo/user", setter);
            String execute = command.execute();
            log.info("result: {}", execute);
        }
    }

    @Test
    public void testQueue() throws ExecutionException, InterruptedException, TimeoutException {
        LinkedList<Future<String>> futures = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            HttpGetCommand command = new HttpGetCommand("http://localhost:8000/demo-provider/demo/user", setter);
            Future<String> future = command.queue();
            futures.add(future);
        }
        for (Future<String> future : futures) {
            String s = future.get(10, TimeUnit.SECONDS);
            log.info("res: {}", s);
        }
    }

    @Test
    public void testObserver() {
        log.info("Start");
        for (int i = 0; i < 5; i++) {
            HttpGetCommand command = new HttpGetCommand("http://localhost:8000/demo-provider/demo/user", setter);
            Observable<String> observe = command.observe();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            observe.subscribe((e) -> log.info("result: {}", e));
            observe.subscribe((e) -> log.info("result: {}", e));
        }
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToObserver() {
        log.info("Start");
        for (int i = 0; i < 5; i++) {
            HttpGetCommand command = new HttpGetCommand("http://localhost:8000/demo-provider/demo/user", setter);
            Observable<String> observe = command.toObservable();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            observe.subscribe((e) -> log.info("result: {}", e));
//            observe.subscribe((e) -> log.info("result: {}", e)); throws exception
        }
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTimeCommand() throws InterruptedException {
        long executeTime = 800;
        for (int i = 0; i < 10; i++) {
            TaskTimeDemoCommand command = new TaskTimeDemoCommand(executeTime, setter);
            String res = command.execute();
            log.info("result: {}", res);

            HystrixCommandMetrics.HealthCounts hc = command.getMetrics().getHealthCounts();
            if(command.isCircuitBreakerOpen()) {
                executeTime = 300;
                log.info("熔断器打开");

                Thread.sleep(7000);
            }
        }
    }

}
