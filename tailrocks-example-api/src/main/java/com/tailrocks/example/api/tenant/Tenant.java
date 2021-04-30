// FIXME rename this package
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
