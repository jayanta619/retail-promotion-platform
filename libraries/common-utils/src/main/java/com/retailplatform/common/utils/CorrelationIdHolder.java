package com.retailplatform.common.utils;

public class CorrelationIdHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void set(String correlationId) {
        CONTEXT.set(correlationId);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
