/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.client.config;

public final class Constants {

    // FIXME replace with correct service name, for example: payment
    public static final String TENANT_SERVICE_NAME = "example";

    // FIXME replace with correct service name, for example: tailrocks.client.payment
    public static final String PREFIX = "tailrocks.client.example";

    public static final String GRPC_CHANNEL = "${" + PREFIX + ".grpc-channel}";
    public static final String DEFAULT_TENANT = PREFIX + ".default-tenant";

    private Constants() {
    }

}
