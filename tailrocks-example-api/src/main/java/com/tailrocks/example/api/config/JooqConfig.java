/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.config;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.jooq.conf.Settings;

@Factory
public class JooqConfig {

    @Bean
    @Singleton
    @Named("default") // don't remove it, it needs to override the default jOOQ settings
    public Settings jooqSettings() {
        return new Settings()
                .withReturnAllOnUpdatableRecord(true)
                .withExecuteWithOptimisticLocking(true)
                .withRenderSchema(false);
    }

}
