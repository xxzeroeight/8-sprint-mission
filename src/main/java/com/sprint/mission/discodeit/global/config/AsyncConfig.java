package com.sprint.mission.discodeit.global.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@EnableRetry
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer
{
    @Bean
    public TaskDecorator mdcTaskDecorator() {
        return runnable -> {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            SecurityContext securityContext = SecurityContextHolder.getContext();

            return () -> {
                try {
                    if (contextMap != null) MDC.setContextMap(contextMap);
                    SecurityContextHolder.setContext(securityContext);
                    runnable.run();
                } finally {
                    MDC.clear();
                    SecurityContextHolder.clearContext();
                }
            };
        };
    }

    @Bean(name = "eventTaskExecutor")
    public ThreadPoolTaskExecutor eventTaskExecutor(TaskDecorator mdcTaskDecorator) {
        return buildExecutor(2, 4, 100, 60, "event-exec", mdcTaskDecorator);
    }

    @Bean(name = "binaryContentTaskExecutor")
    public ThreadPoolTaskExecutor binaryContentTaskExecutor(TaskDecorator taskDecorator) {
        return buildExecutor(4, 8, 100, 120, "binarycontent-exec", taskDecorator);
    }

    @Bean(name = "notificationTaskExecutor")
    public ThreadPoolTaskExecutor notificationTaskExecutor(TaskDecorator taskDecorator) {
        return buildExecutor(4, 8, 400, 60, "notification-exec", taskDecorator);
    }

    private ThreadPoolTaskExecutor buildExecutor(int core,
                                                 int max,
                                                 int queue,
                                                 int keepAlive,
                                                 String prefix,
                                                 TaskDecorator decorator)
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(max);
        executor.setQueueCapacity(queue);
        executor.setKeepAliveSeconds(keepAlive);
        executor.setThreadNamePrefix(prefix + "-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(20);
        executor.setTaskDecorator(decorator);
        executor.initialize();

        return executor;
    }
}

