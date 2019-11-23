package com.mr.common;

import java.util.concurrent.atomic.AtomicInteger;

public class GlobalUniqueNo {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static Integer get(){
        return atomicInteger.getAndIncrement();
    }
}
