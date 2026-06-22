package com.ncrde.church.service;

import com.ncrde.church.entity.Member;
import com.ncrde.church.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SchedulingService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulingService.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job nightlyCleanupJob;

    // Run every day at midnight to check for birthdays
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkBirthdays() {
        logger.info("Scheduling Task: Checking for member birthdays today...");
        LocalDate today = LocalDate.now();
        List<Member> members = memberRepository.findAll().stream()
                .filter(m -> !m.isDeleted())
                .filter(m -> m.getDob() != null 
                          && m.getDob().getMonth() == today.getMonth() 
                          && m.getDob().getDayOfMonth() == today.getDayOfMonth())
                .toList();

        for (Member member : members) {
            logger.info("Happy Birthday to: {} {}!", member.getFirstName(), member.getLastName());
            // Send email/SMS or push dynamic alert via websocket
        }
    }

    // Run the Spring Batch cleanup job every night at 2:00 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void runCleanupJob() {
        try {
            logger.info("Triggering Batch Cleanup Job...");
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(nightlyCleanupJob, params);
        } catch (Exception e) {
            logger.error("Failed to execute cleanup batch job", e);
        }
    }
}
