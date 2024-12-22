package com.zcc.highmyopia.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
/**
 * @Author zcc
 * @Date 2024/12/19
 * @Description 建立线程池，进行异步任务，不阻塞主进程
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor asyncExecutor() {
        return new ThreadPoolTaskExecutor();  // 配置线程池
    }
}
