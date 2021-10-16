package com.jwl;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;
import rx.Observer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RxJavaTest {

    @Test
    public void testCreate() throws InterruptedException {
        Observable.just("1", "2", "3").subscribe(System.out::print);
        System.out.println();
        Observable.from(new Integer[] {1,2,3}).subscribe(System.out::print);
        System.out.println();
        Observable.range(1, 10).subscribe(System.out::print);
        System.out.println();
        Observable.defer(() -> Observable.just(10)).subscribe(System.out::print);
        System.out.println();
        Observable.range(1, 20).filter((x) -> x % 5 == 0).subscribe((e) -> System.out.print(e + ","));
        System.out.println();
        Observable.range(1, 4).map((x) -> x * x).subscribe((e) -> System.out.print(e + ","));
        System.out.println();
        Observable.range(1, 4).flatMap((i) -> Observable.range(1, i)).subscribe((e) -> System.out.print(e + ","));
    }

    public static void main(String[] args) throws InterruptedException {
        Observable
                .interval(1000, TimeUnit.MILLISECONDS)
                .window(3000, TimeUnit.MILLISECONDS)
                .flatMap(Observable::toList)
                .subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1000);
    }

    @Test
    public void windowDemo() throws InterruptedException {
        Random random = new Random();

        Observable<Integer> eventStream = Observable
                .interval(100, TimeUnit.MILLISECONDS)
                .map((i) -> random.nextInt(2));

        Observable<Long> bucketStream = eventStream
                .window(300, TimeUnit.MILLISECONDS)
                .flatMap((observable) -> observable.toList().map((list) -> {
                    long count = list.stream().filter((i) -> i == 0).count();
                    log.info("{} failure count {}", list, count);
                    return count;
                }));

        bucketStream
                .window(3)
                .flatMap((observable) -> observable.reduce(Long::sum)).subscribe((e) -> log.info("窗口失败次数 {}", e));
        Thread.sleep(10000);
    }

}
