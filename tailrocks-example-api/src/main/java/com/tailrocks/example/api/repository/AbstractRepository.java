/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.repository;

import com.tailrocks.example.api.tenant.Tenant;
import org.jooq.DSLContext;

// TODO move to jambalaya
public abstract class AbstractRepository {

    private final DSLContext dslContext;

    protected AbstractRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    protected DSLContext getDslContext(Tenant tenant) {
        dslContext.setSchema(tenant.getSchema()).execute();
        return dslContext;
    }

}
