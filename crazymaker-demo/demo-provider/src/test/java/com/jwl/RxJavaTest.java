package com.jwl;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Single;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;
import rx.Subscriber;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.observers.Subscribers;
import rx.subscriptions.Subscriptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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


     @Test
    public void testConnectableObservable() {
         Observable<Object> observable = Observable.create(s -> {
             System.out.println("connected");
             s.onNext(1);
             s.onNext(2);
//             s.onCompleted();
             s.add(Subscriptions.create(() -> System.out.println("disconnected")));
         });
//         Subscription subscribe = observable.subscribe(System.out::println);
//         Subscription subscribe1 = observable.subscribe(System.out::println);
//         subscribe.unsubscribe();
//         subscribe1.unsubscribe();
//         Observable<Object> refCount = observable.publish().refCount();
//         Observable<Object> refCount = observable.share();
         ConnectableObservable<Object> publish = observable.publish();
         Subscription subscribe = publish.subscribe(System.out::println);
         Subscription subscribe1 = publish.subscribe(System.out::println);
         publish.connect();
         subscribe.unsubscribe();
         System.out.println(123);
         subscribe1.unsubscribe();
    }

    @Test
    public void testFlatMap() {
        Observable<Integer> range = Observable.range(0, 100);
        Observable<String> complete = range.flatMap(e -> Observable.empty(), Observable::error, () -> Observable.just("complete"));
        complete.subscribe(System.out::println, System.out::println, System.out::println);
    }

    @Test
    public void square() {
        Observable<Integer> oneToEight = Observable.range(1, 8);
        Observable<String> rank = oneToEight.map(Objects::toString);
        Observable<String> file = oneToEight.map(i -> 'a' + i - 1)
                .map(i -> (char) i.intValue())
                .map(i -> Character.toString(i));
        file.flatMap(f -> rank.map(r -> f + r)).subscribe(System.out::println);
    }

    @Test
    public void wordLengthDelay() throws InterruptedException {
        ArrayList<String> words = new ArrayList<String>() {{
            add("Thought");
            add("this");
            add("word");
        }};
        Observable<String> word = Observable.from(words);
        word.flatMap((w) -> {
            System.out.println(w);
            return Observable.just(w).delay(w.length(), TimeUnit.SECONDS);
        }).subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(100);
    }

    @Test
    public void wordLengthDelay1() throws InterruptedException {
        ArrayList<String> words = new ArrayList<String>() {{
            add("Thought");
            add("this");
            add("word");
        }};
        Observable<String> word = Observable.from(words);
        Observable<Integer> length = word.map(String::length).scan(Integer::sum).startWith(0);
        word.zipWith(length, Pair::of)
            .flatMap((pair) -> Observable.just(pair.getLeft()).delay(pair.getRight(), TimeUnit.SECONDS))
            .subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(100);
    }

    @Test
    public void testZip() {
        Observable<Integer> just = Observable.just(1, 2, 3);
        Observable<Integer> just1 = Observable.just(1, 2);
        just.zipWith(just1, (a, b) -> a + ":" + b).subscribe(System.out::println);
    }

    @Test
    public void testGroupBy() {
        Observable<GroupedObservable<String, Integer>> groupedObservableObservable = Observable.just(1, 2, 2, 3, 1, 2).groupBy(String::valueOf);
        groupedObservableObservable.subscribe((ob) -> {
            System.out.println("---------------------" + Thread.currentThread().getName());
            ob.forEach(System.out::println);
        });
    }

    @Test
    public void testGroupBy1() throws InterruptedException {
        Random random = new Random();
        Observable<Object> observable = Observable.create((s) -> {
            new Thread(() -> {
                while (true) {
                    s.onNext(random.nextInt(10));
                }
            }).start();
        });
        Observable<GroupedObservable<String, Object>> groupedObservableObservable = observable.groupBy(String::valueOf);
        groupedObservableObservable.subscribe((ob) -> {
            System.out.println("---------------------" + Thread.currentThread().getName());
            ob.forEach(System.out::println);
        });
        TimeUnit.SECONDS.sleep(100);
    }


    @Test
    public void testSchedule() {
        Observable.just("apple", "banana", "cheese", "tomato")
                .map((e) -> {
                    logMsg(e);
                    return e;
                })
                .subscribeOn(Schedulers.io())
                .subscribe(RxJavaTest::logMsg);
    }



    private static void logMsg(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }
}
