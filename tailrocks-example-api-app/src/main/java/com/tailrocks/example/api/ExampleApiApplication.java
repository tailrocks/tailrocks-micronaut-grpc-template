/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api;

import com.tailrocks.example.jooq.tables.records.PaymentMethodRecord;
import io.micronaut.core.annotation.TypeHint;
import io.micronaut.runtime.Micronaut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.util.TimeZone;

@TypeHint(
        value = {
                PaymentMethodRecord.class
        },
        accessType = {TypeHint.AccessType.ALL_DECLARED_CONSTRUCTORS}
)
// FIXME rename this class, for example: PaymentApiApplication
public class ExampleApiApplication {

    private static final Logger log = LoggerFactory.getLogger(ExampleApiApplication.class);

    static {
        log.debug("Setting UTC time zone by default");
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
    }

    public static void main(String[] args) {
        Micronaut.run(ExampleApiApplication.class, args);
    }

}
