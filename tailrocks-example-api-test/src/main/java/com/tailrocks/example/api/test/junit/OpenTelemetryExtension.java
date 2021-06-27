package com.tailrocks.example.api.test.junit;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

/**
 * @author Alexey Zhokhov
 */
public class OpenTelemetryExtension implements BeforeEachCallback, AfterEachCallback, AfterAllCallback {

    private final Map<String, Scope> scopes = new HashMap<>();
    private final Map<String, Span> spans = new HashMap<>();

    @Override
    public void beforeEach(ExtensionContext context) {
        var tracer = GlobalOpenTelemetry.get().getTracer("test");

        Span span = tracer
                .spanBuilder(context.getRequiredTestMethod().getName())
                .startSpan();

        out.println(context.getRequiredTestClass().getSimpleName() + " > " + context.getRequiredTestMethod().getName());
        out.println("Trace ID: " + span.getSpanContext().getTraceId());

        spans.put(context.getUniqueId(), span);
        scopes.put(context.getUniqueId(), span.makeCurrent());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        spans.get(context.getUniqueId()).end();
        scopes.get(context.getUniqueId()).close();
        scopes.remove(context.getUniqueId());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        for (var entry : scopes.entrySet()) {
            entry.getValue().close();
        }
        for (var entry : spans.entrySet()) {
            entry.getValue().end();
        }
        scopes.clear();
    }

}
