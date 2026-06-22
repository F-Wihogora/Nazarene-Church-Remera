package com.ncrde.church.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfig.class);

    @Bean
    public Job nightlyCleanupJob(JobRepository jobRepository, Step cleanupStep) {
        return new JobBuilder("nightlyCleanupJob", jobRepository)
                .start(cleanupStep)
                .build();
    }

    @Bean
    public Step cleanupStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    logger.info("Executing Batch Job: cleaning up old audit logs and notifications...");
                    // Task logic runs here
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
