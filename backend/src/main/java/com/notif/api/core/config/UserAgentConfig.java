package com.notif.api.core.config;

import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class for initializing a UserAgentAnalyzer bean.
 * The analyzer is configured to parse user agent strings and extract fields like Operating System class, OS name, and
 * Agent name.
 */
@Configuration
public class UserAgentConfig {
    private static final int CACHE_SIZE = 10000;

    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withField("OperatingSystemClass")
                .withField("OperatingSystemName")
                .withField("AgentName")
                .withCache(CACHE_SIZE)
                .build();
    }
}