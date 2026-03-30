package com.liuheng.context;

public class BaseContext {
    // ThreadLocal 是 Java 提供的线程本地存储机制，让每个线程拥有自己独立的变量副本，线程之间互不干扰。
    public static ThreadLocal<Long> context = new ThreadLocal<>();

    public static Long getCurrentId() {
        return context.get();
    }

    public static void setCurrentId(Long id) {
        context.set(id);
    }

    public static void clearCurrentId() {
        context.remove();
    }

}
