/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.tenant;

public enum Tenant {

    MAIN("public"),
    TESTING("test");

    private String schema;

    Tenant(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return schema;
    }

}
