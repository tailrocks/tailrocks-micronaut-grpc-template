/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.repository;

import com.zhokhov.jambalaya.tenancy.Tenant;
import io.micronaut.context.annotation.Property;
import org.jooq.DSLContext;

import static com.zhokhov.jambalaya.tenancy.TenancyUtils.getTenantOrThrow;

// TODO move to jambalaya
public abstract class AbstractRepository {

    private final DSLContext dslContext;

    @Property(name = "micronaut.application.name")
    String applicationName;

    protected AbstractRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    protected DSLContext getDslContext() {
        String tenant = getTenantOrThrow().getByService(applicationName);
        dslContext.setSchema(getSchema(tenant)).execute();
        return dslContext;
    }

    private String getSchema(String tenant) {
        if (tenant.equals(Tenant.DEFAULT)) {
            return "public";
        } else if (tenant.equals("testing")) {
            return "test";
        } else {
            return tenant;
        }
    }

}
