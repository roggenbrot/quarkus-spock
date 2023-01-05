package io.quarkiverse.test.spock.junit;

import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SpockInvocationContext<T> implements ReflectiveInvocationContext<Constructor<T>> {

    private final T testSpec;

    public SpockInvocationContext(T testSpec) {
        this.testSpec = testSpec;
    }


    @Override
    public Class<?> getTargetClass() {
        return testSpec.getClass();
    }

    @Override
    public Constructor<T> getExecutable() {
        throw new UnsupportedOperationException("This feature is currently not supported by quarkus-spock");
    }

    @Override
    public List<Object> getArguments() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Object> getTarget() {
        return Optional.empty();
    }
}
