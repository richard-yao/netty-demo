package com.richard.study.nettydemo.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: richard_xsyao
 * @date: 2018/9/4 14:40
 * Description:
 */
public class CustomThreadPool {

    private ExecutorService executorService;

    private CustomThreadPool() {
    }

    public static ExecutorService getThreadPool() {
        CustomThreadPool threadPool = new CustomThreadPool();
        return threadPool.createFixedThreadPool();
    }

    /**
     * 手动创建Scheduled线程池, 初始线程数量为3，最大数量默认为Integer.MAX_VALUE，极端情况下有可能导致OOM
     *
     * @return
     */
    public ExecutorService createScheduledThreadPool() {
        int corePoolSize = 3;
        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("pool-thread-%d").daemon(true).build();
        executorService = new ScheduledThreadPoolExecutor(corePoolSize, threadFactory, new ThreadPoolExecutor.AbortPolicy());
        return executorService;
    }

    /**
     * 手动创建初始线程数量3，最大线程数量5，最大等待队列1024，默认线程剔除策略为AbortPolicy的线程池
     *
     * @return
     */
    public ExecutorService createFixedThreadPool() {
        int corePoolSize = 3;
        int maxPoolSize = 5;
        long keepAliveTime = 0;
        ThreadFactory commonFactory = new BasicThreadFactory.Builder().namingPattern("pool-thread-%d").daemon(true).build();
        executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(1024), commonFactory, new ThreadPoolExecutor.AbortPolicy());
        return executorService;
    }
}
