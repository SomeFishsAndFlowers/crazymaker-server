package com.jwl;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
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

    @Test
    public void parallel() throws InterruptedException {
        Observable<Object> a = Observable.create(s -> {
            new Thread(() -> {
                s.onNext(1);
                s.onNext(2);
                s.onCompleted();
            }).start();
        });

        Observable<Object> b = Observable.create(s -> {
            new Thread(() -> {
                s.onNext(3);
                s.onNext(4);
                s.onCompleted();
            }).start();
        });

        Observable.merge(a, b).subscribe((e) -> {
            System.out.println(Thread.currentThread() + ": " + e);
        });
        Thread.sleep(1000);
        System.out.println(Thread.currentThread());
    }

    @Test
    public void testFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> a = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        });
        CompletableFuture<Integer> b = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        });

        CompletableFuture<Integer> completableFuture = a.thenCombine(b, Integer::sum);

        System.out.println(completableFuture.get());
    }

    @Test
    public void testSingle() throws InterruptedException {
        Single<String> hello = Single.<String>create(s -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("hello");
            s.onSuccess("hello");
        }).subscribeOn(Schedulers.io());
        Single<String> world = Single.<String>create(s -> {
            log.info("world");
            s.onSuccess("world");
        }).subscribeOn(Schedulers.io());
//        hello.mergeWith(world).subscribe(s -> {
//            log.info("is daemon: {}, sub: {}", Thread.currentThread().isDaemon(), s);
//        });
        log.info("before");
        hello.mergeWith(world);
        log.info("after");
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void testSingle2() throws InterruptedException {
        Single<String> hello = Single.<String>create(s -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("hello");
            s.onSuccess("hello");
        }).subscribeOn(Schedulers.io());
        Single<String> world = Single.<String>create(s -> {
            log.info("world");
            s.onSuccess("world");
        }).subscribeOn(Schedulers.io());
        Single.merge(hello, world).subscribe((s) -> {
            log.info("is daemon: {}, sub: {}", Thread.currentThread().isDaemon(), s);
        });
//        log.info("before");
//        Single.merge(hello, world);
//        log.info("after");
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void testError() {
        Observable.create(s -> {
            s.onNext(1);
            s.onError(new Exception("error"));
        }).subscribe(System.out::println, System.out::println);
        System.out.println(2);
    }

}
