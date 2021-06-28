package com.tailrocks.example.api.test.junit;

/**
 * @author Alexey Zhokhov
 */
@FunctionalInterface
public interface CheckedRunnable {

    void run() throws Throwable;

}
