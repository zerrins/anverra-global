package com.anverraglobal;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import java.util.concurrent.Executor;

@Configuration
@EnableScheduling
@EnableAsync
public class ApplicationConfiguration implements AsyncConfigurer {

    @Bean(name = "modulithEventExecutor")
    public ThreadPoolTaskExecutor modulithEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("modulith-event-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return modulithEventExecutor();
    }
}
