package io.quarkiverse.test.spock.junit;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExecutableInvoker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.platform.engine.support.hierarchical.OpenTest4JAwareThrowableCollector;
import org.junit.platform.engine.support.hierarchical.ThrowableCollector;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.SpecInfo;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class SpockExtensionContext implements ExtensionContext {

    private final SpecInfo specInfo;

    private final ThrowableCollector collector;

    private final IMethodInvocation method;

    private final Store store;

    public SpockExtensionContext(SpecInfo specInfo, Store store, IMethodInvocation method) {
        this.specInfo = specInfo;
        this.method = method;
        this.store = store;
        collector = new OpenTest4JAwareThrowableCollector();
    }

    @Override
    public Optional<ExtensionContext> getParent() {
        return Optional.empty();
    }

    @Override
    public ExtensionContext getRoot() {
        return this;
    }

    @Override
    public String getUniqueId() {
        return "[engine:spock]";
    }

    @Override
    public String getDisplayName() {
        return "Spock Framework";
    }


    @Override
    public Set<String> getTags() {
        return Collections.emptySet();
    }

    @Override
    public Optional<AnnotatedElement> getElement() {
        return Optional.empty();
    }

    @Override
    public Optional<Class<?>> getTestClass() {
        return Optional.ofNullable(specInfo.getReflection());
    }

    @Override
    public Optional<TestInstance.Lifecycle> getTestInstanceLifecycle() {
        return Optional.of(TestInstance.Lifecycle.PER_METHOD);
    }

    @Override
    public Optional<Object> getTestInstance() {
        return Optional.empty();
    }

    @Override
    public Optional<TestInstances> getTestInstances() {
        return Optional.empty();
    }

    @Override
    public Optional<Method> getTestMethod() {
        return Optional.ofNullable(method).map(m-> m.getMethod().getReflection());
    }

    @Override
    public Optional<Throwable> getExecutionException() {
        return Optional.ofNullable(collector.getThrowable());
    }

    @Override
    public Optional<String> getConfigurationParameter(String key) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> getConfigurationParameter(String key, Function<String, T> transformer) {
        return Optional.empty();
    }

    @Override
    public void publishReportEntry(Map<String, String> map) {

    }

    @Override
    public Store getStore(Namespace namespace) {
        return store;
    }

    @Override
    public ExecutionMode getExecutionMode() {
        final org.spockframework.runtime.model.parallel.ExecutionMode executionMode =
            Optional.ofNullable(specInfo)
                .map(info -> info.getExecutionMode()
                    .orElse(org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD)
                )
                .orElse(org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD);
        return ExecutionMode.valueOf(executionMode.name());
    }


    @Override
    public ExecutableInvoker getExecutableInvoker() {
        throw new UnsupportedOperationException();
    }
}
