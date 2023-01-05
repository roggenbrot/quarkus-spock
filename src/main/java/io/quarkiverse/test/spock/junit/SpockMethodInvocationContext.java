package io.quarkiverse.test.spock.junit;

import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.MethodInfo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpockMethodInvocationContext implements ReflectiveInvocationContext<Method> {


    private final Class<?> targetClass;
    private final Method method;
    private final List<Object> arguments;

    private final Object target;


    public SpockMethodInvocationContext(IMethodInvocation methodInvocation) {
        targetClass = methodInvocation.getTarget().getClass();
        method = methodInvocation.getMethod().getReflection();
        arguments = Arrays.asList(methodInvocation.getArguments());
        target = methodInvocation.getTarget();
    }

    public SpockMethodInvocationContext(IMethodInvocation methodInvocation, MethodInfo methodInfo) {
        targetClass = methodInvocation.getTarget().getClass();
        method = methodInfo.getReflection();
        arguments = methodInfo.getParameters().stream()
            .map(m -> (Object) m.getReflection())
            .collect(Collectors.toList());
        target = methodInvocation.getTarget();
    }

    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    @Override
    public Method getExecutable() {
        return method;
    }

    @Override
    public List<Object> getArguments() {
        return arguments;
    }

    @Override
    public Optional<Object> getTarget() {
        return Optional.of(target);
    }
}
