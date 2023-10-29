package com.kyc.postalcodes.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.kyc.postalcodes.constants.AppConstants.CACHE_VERSION;

@Configuration
@EnableScheduling
public class CacheCleanSchedulingConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheCleanSchedulingConfig.class);

    @Scheduled(fixedRate = 300_000)
    @CacheEvict(cacheNames = CACHE_VERSION,allEntries = true)
    public void cleanCache(){
        LOGGER.info("Clean cache");
    }
}
