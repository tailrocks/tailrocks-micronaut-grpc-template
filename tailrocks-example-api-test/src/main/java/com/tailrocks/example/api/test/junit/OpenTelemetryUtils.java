package com.tailrocks.example.api.test.junit;

import com.zhokhov.jambalaya.tenancy.UncheckedCallable;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

import static java.lang.System.out;

/**
 * @author Alexey Zhokhov
 */
public final class OpenTelemetryUtils {

    private static final String GIVEN_PREFIX = "GIVEN:";
    private static final String WHEN_PREFIX = "WHEN:";
    private static final String THEN_PREFIX = "THEN:";

    private OpenTelemetryUtils() {
    }

    public static void GIVEN(Runnable runnable) {
        WHEN(null, runnable);
    }

    public static void GIVEN(@Nullable String description, Runnable runnable) {
        invokeRunnable(GIVEN_PREFIX, description, runnable);
    }

    public static void WHEN(Runnable runnable) {
        WHEN(null, runnable);
    }

    public static void WHEN(@Nullable String description, Runnable runnable) {
        invokeRunnable(WHEN_PREFIX, description, runnable);
    }

    public static void THEN(Runnable runnable) {
        THEN(null, runnable);
    }

    public static void THEN(@Nullable String description, Runnable runnable) {
        invokeRunnable(THEN_PREFIX, description, runnable);
    }

    public static <T> T GIVEN_(UncheckedCallable<T> callable) {
        return WHEN_(null, callable);
    }

    public static <T> T GIVEN_(@Nullable String description, UncheckedCallable<T> callable) {
        return invokeCallable(GIVEN_PREFIX, description, callable);
    }

    public static <T> T WHEN_(UncheckedCallable<T> callable) {
        return WHEN_(null, callable);
    }

    public static <T> T WHEN_(@Nullable String description, UncheckedCallable<T> callable) {
        return invokeCallable(WHEN_PREFIX, description, callable);
    }

    public static <T> T THEN_(UncheckedCallable<T> callable) {
        return THEN_(null, callable);
    }

    public static <T> T THEN_(@Nullable String description, UncheckedCallable<T> callable) {
        return invokeCallable(THEN_PREFIX, description, callable);
    }

    private static void invokeRunnable(String prefix, @Nullable String description, Runnable runnable) {
        Span span = startSpan(prefix, description);

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

    private static <T> T invokeCallable(String prefix, @Nullable String description, UncheckedCallable<T> callable) {
        Span span = startSpan(prefix, description);

        try (Scope ignored = span.makeCurrent()) {
            return callable.call();
        } catch (Throwable t) {
            span.recordException(t);
            span.setStatus(StatusCode.ERROR);
            throw t;
        } finally {
            span.end();
        }
    }

    private static Span startSpan(String prefix, String description) {
        Tracer tracer = GlobalOpenTelemetry.getTracer("test");
        String spanName = prefix + (description != null ? " " + description : "");
        Span span = tracer.spanBuilder(spanName).startSpan();

        out.println(">> " + spanName);
        return span;
    }

}
