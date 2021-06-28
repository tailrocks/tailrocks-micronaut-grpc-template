package com.tailrocks.example.api.test.junit;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static io.opentelemetry.semconv.trace.attributes.SemanticAttributes.CODE_FUNCTION;
import static io.opentelemetry.semconv.trace.attributes.SemanticAttributes.CODE_NAMESPACE;
import static io.opentelemetry.semconv.trace.attributes.SemanticAttributes.THREAD_ID;
import static io.opentelemetry.semconv.trace.attributes.SemanticAttributes.THREAD_NAME;
import static java.lang.System.out;

/**
 * @author Alexey Zhokhov
 */
public class OpenTelemetryExtension implements BeforeEachCallback, AfterEachCallback, AfterAllCallback,
        InvocationInterceptor {

    private static Tracer tracer;

    private final Map<String, Scope> scopes = new HashMap<>();
    private final Map<String, Span> spans = new HashMap<>();

    private static Span startSpan(String spanName) {
        if (tracer == null) {
            tracer = GlobalOpenTelemetry.get().getTracer("junit5-extension");
        }

        return tracer
                .spanBuilder(spanName)
                .startSpan();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Span span = startSpan(context.getDisplayName());

        span.setAttribute(THREAD_ID, Thread.currentThread().getId());
        span.setAttribute(THREAD_NAME, Thread.currentThread().getName());
        span.setAttribute(CODE_FUNCTION, context.getRequiredTestMethod().getName());
        span.setAttribute(CODE_NAMESPACE, context.getRequiredTestClass().getName());

        out.println(context.getRequiredTestClass().getSimpleName() + " > " + context.getDisplayName());
        out.println("Trace ID: " + span.getSpanContext().getTraceId());

        spans.put(context.getUniqueId(), span);
        scopes.put(context.getUniqueId(), span.makeCurrent());
    }

    @Override
    public void afterEach(ExtensionContext context) {
        context.getExecutionException().ifPresent(throwable -> {
            spans.get(context.getUniqueId()).recordException(throwable);
            spans.get(context.getUniqueId()).setStatus(StatusCode.ERROR);
        });
        scopes.get(context.getUniqueId()).close();
        scopes.remove(context.getUniqueId());
        spans.get(context.getUniqueId()).end();
        spans.remove(context.getUniqueId());
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
        spans.clear();
    }

    @Override
    public void interceptBeforeAllMethod(Invocation<Void> invocation,
                                         ReflectiveInvocationContext<Method> invocationContext,
                                         ExtensionContext extensionContext) throws Throwable {
        interceptMethod("@BeforeAll: ", invocationContext, () ->
                InvocationInterceptor.super.interceptBeforeAllMethod(invocation, invocationContext, extensionContext)
        );
    }

    @Override
    public void interceptAfterAllMethod(Invocation<Void> invocation,
                                        ReflectiveInvocationContext<Method> invocationContext,
                                        ExtensionContext extensionContext) throws Throwable {
        interceptMethod("@AfterAll: ", invocationContext, () ->
                InvocationInterceptor.super.interceptAfterAllMethod(invocation, invocationContext, extensionContext)
        );
    }

    @Override
    public void interceptBeforeEachMethod(Invocation<Void> invocation,
                                          ReflectiveInvocationContext<Method> invocationContext,
                                          ExtensionContext extensionContext) throws Throwable {
        interceptMethod("@BeforeEach: ", invocationContext, () ->
                InvocationInterceptor.super.interceptBeforeEachMethod(invocation, invocationContext, extensionContext)
        );
    }

    @Override
    public void interceptAfterEachMethod(Invocation<Void> invocation,
                                         ReflectiveInvocationContext<Method> invocationContext,
                                         ExtensionContext extensionContext) throws Throwable {
        interceptMethod("@AfterEach: ", invocationContext, () ->
                InvocationInterceptor.super.interceptAfterEachMethod(invocation, invocationContext, extensionContext)
        );
    }

    private void interceptMethod(String prefix, ReflectiveInvocationContext<Method> invocationContext,
                                 CheckedRunnable runnable) throws Throwable {
        String spanName = prefix + invocationContext.getExecutable().getName();

        Span span = startSpan(spanName);

        span.setAttribute(THREAD_ID, Thread.currentThread().getId());
        span.setAttribute(THREAD_NAME, Thread.currentThread().getName());
        span.setAttribute(CODE_FUNCTION, invocationContext.getExecutable().getName());
        span.setAttribute(CODE_NAMESPACE, invocationContext.getTargetClass().getName());

        out.println(">> " + spanName);

        try (Scope ignored = span.makeCurrent()) {
            runnable.run();
        } catch (Throwable t) {
            span.recordException(t);
            span.setStatus(StatusCode.ERROR);
            throw t;
        } finally {
            span.end();
        }
    }
}
