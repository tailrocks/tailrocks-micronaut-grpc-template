package com.tailrocks.example.api.test.junit;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.context.Scope;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexey Zhokhov
 */
public class OpenTelemetryExtension implements BeforeTestExecutionCallback, BeforeEachCallback, AfterEachCallback,
        AfterAllCallback {

    private final Map<String, Scope> scopes = new HashMap<>();

    public OpenTelemetryExtension() {
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        var tracer = GlobalOpenTelemetry.get().getTracer("test");

        var span = tracer
                .spanBuilder(context.getRequiredTestMethod().getName())
                .startSpan();

        System.out.println(context.getRequiredTestMethod().getName() + " TRACE ID: " + span.getSpanContext().getTraceId());

        scopes.put(context.getUniqueId(), span.makeCurrent());
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        scopes.get(context.getUniqueId()).close();
        scopes.remove(context.getUniqueId());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        for (String key : scopes.keySet()) {
            scopes.get(key).close();
        }
        scopes.clear();
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println("XXX");
    }
}
