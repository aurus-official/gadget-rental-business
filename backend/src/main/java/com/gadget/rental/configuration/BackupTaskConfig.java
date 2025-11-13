package com.gadget.rental.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gadget.rental.backup.BackupTask;

@Configuration
public class BackupTaskConfig {

    @Bean
    BackupTask getBackupTask() {
        return new BackupTask();
    }
}
