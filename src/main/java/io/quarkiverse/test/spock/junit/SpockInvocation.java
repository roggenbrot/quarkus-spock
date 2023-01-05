package io.quarkiverse.test.spock.junit;

import org.junit.jupiter.api.extension.InvocationInterceptor;

public class SpockInvocation<T> implements InvocationInterceptor.Invocation<T> {

    private boolean proceed = false;

    private final T value;

    public SpockInvocation(T value) {
        this.value = value;
    }

    @Override
    public T proceed() throws Throwable {
        proceed = true;
        return value;
    }


    public boolean shouldProceed() {
        return proceed;
    }
}
