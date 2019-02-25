package com.ntnikka.common.utils;

import static java.util.concurrent.CompletableFuture.runAsync;

/**
 * @ClassName runAsyncTestUtil
 * @Author Liuq
 * @Description todo
 * @Date 2019/1/30 9:50
 **/
public class runAsyncTestUtil {
    public static void main(String[] args) throws Exception{
        for(int i = 0 ; i < 100 ; i++){
            method(i , Thread.currentThread().getName());
//            Thread.sleep(1000);
        }
    }


    public static void method(int i , String SynchronizedThreadName){
        runAsync(() -> {
            System.out.println("count"+i+",SynchronizedThreadName : "+SynchronizedThreadName+",AsyncThreadName : "+Thread.currentThread().getName());
        });
    }
}
