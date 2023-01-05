package io.quarkiverse.test.spock;

import io.quarkiverse.test.spock.junit.SpockExtensionContext;
import io.quarkiverse.test.spock.junit.SpockExtensionStore;
import io.quarkiverse.test.spock.junit.SpockInvocation;
import io.quarkiverse.test.spock.junit.SpockInvocationContext;
import io.quarkiverse.test.spock.junit.SpockMethodInvocationContext;
import io.quarkus.test.junit.QuarkusTestExtension;
import io.quarkus.test.junit.QuarkusTestExtensionState;
import org.junit.platform.commons.util.Preconditions;
import org.spockframework.runtime.extension.IAnnotationDrivenExtension;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.MethodInfo;
import org.spockframework.runtime.model.SpecInfo;

public class QuarkusSpockExtension implements IAnnotationDrivenExtension<QuarkusSpockTest> {


    @Override
    public void visitSpecAnnotation(QuarkusSpockTest annotation, SpecInfo specInfo) {
        new QuarkusTestExtensionDelegate(specInfo).init();
    }

    /**
     * Delegate Spock lifecycle methods to JUnit5 handler methods of the quarkus JUnit5
     * extension
     */
    private static class QuarkusTestExtensionDelegate extends QuarkusTestExtension {


        private final SpockExtensionStore store;
        private final SpecInfo specInfo;


        public QuarkusTestExtensionDelegate(SpecInfo specInfo) {
            this.specInfo = specInfo;
            this.store = new SpockExtensionStore();
        }

        /**
         * Init the delegate:
         * <p>
         * Register spock lifecycle interceptors that delegates the events to the
         * quarkus JUnit5 extension
         */
        public void init() {
            specInfo.addInitializerInterceptor(this::onInitializer);
            specInfo.addSetupSpecInterceptor(this::onSetupSpec);
            specInfo.addSetupInterceptor(this::onSetup);
            specInfo.addCleanupInterceptor(this::onClean);
            specInfo.addCleanupSpecInterceptor(this::onCleanSpec);
            specInfo.getAllFeatures().forEach(
                featureInfo -> featureInfo.getFeatureMethod().addInterceptor(this::onFeature)
            );
        }


        /**
         * Handle "setupSpec" lifecycle for spock spec
         *
         * @param specInfo SpecInfo of the spock specification target
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of any setupSpec lifecycle methods
         */
        private void onSetupSpec(SpecInfo specInfo, IMethodInvocation invocation) throws Throwable {
            if (specInfo != null) {
                // Call the quarkus junit5 extension for all specified setupSpec methods so that all
                // of them are invoked in the classloader context of quarkus
                for (MethodInfo methodInfo : specInfo.getSetupSpecMethods()) {
                    interceptBeforeAllMethod(
                        new SpockInvocation<>(null),
                        new SpockMethodInvocationContext(invocation, methodInfo),
                        new SpockExtensionContext(specInfo, store, invocation)
                    );
                }
                // Make sure that setupSpec methods in super class are also invoked
                onSetupSpec(specInfo.getSuperSpec(), invocation);
            }
        }

        /**
         * Handle "setupSpec" lifecycle for current spock spec and its super classes
         *
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of any setupSpec lifecycle methods
         */
        private void onSetupSpec(IMethodInvocation invocation) throws Throwable {
            beforeAll(new SpockExtensionContext(specInfo, store, invocation));
            onSetupSpec(specInfo, invocation);
        }

        /**
         * Handle "initialize" lifecycle event
         *
         * @param invocation Invocation
         * @throws Throwable On exception during creation of test specification instance
         */
        private void onInitializer(IMethodInvocation invocation) throws Throwable {
            final Object specInstance = Preconditions.notNull(invocation.getInstance(), "No spec instance");
            // Call junit5 extension so that the extension is able to create an own object instance of
            // the specification inside the quarkus classloader context
            interceptTestClassConstructor(
                new SpockInvocation<>(specInstance),
                new SpockInvocationContext<>(specInstance),
                new SpockExtensionContext(specInfo, store, invocation)
            );
        }

        /**
         * Handle "setup" lifecycle for spock spec
         *
         * @param specInfo SpecInfo of the spock specification target
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of any setup lifecycle methods
         */
        private void onSetup(SpecInfo specInfo, IMethodInvocation invocation) throws Throwable {
            if (specInfo != null) {
                // Call the quarkus junit5 extension for all specified setup methods so that all
                // of them are invoked in the classloader context of quarkus
                for (MethodInfo methodInfo : specInfo.getSetupMethods()) {
                    interceptBeforeEachMethod(
                        new SpockInvocation<>(null),
                        new SpockMethodInvocationContext(invocation, methodInfo),
                        new SpockExtensionContext(specInfo, store, invocation)
                    );
                }
                // Make sure that setup methods in super class are also invoked
                onSetup(specInfo.getSuperSpec(), invocation);
            }
        }

        /**
         * Handle "setup" lifecycle for current spock spec and its super classes
         *
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of any setup lifecycle methods
         */
        private void onSetup(IMethodInvocation invocation) throws Throwable {
            onSetup(specInfo, invocation);
        }

        /**
         * Handle "feature" lifecycle (test case execution) for current spock spec
         *
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of the test case
         */
        private void onFeature(IMethodInvocation invocation) throws Throwable {
            beforeEach(new SpockExtensionContext(specInfo, store, invocation));
            try {
                interceptTestMethod(
                    new SpockInvocation<>(null),
                    new SpockMethodInvocationContext(invocation),
                    new SpockExtensionContext(specInfo, store, invocation)

                );
            } finally {
                afterEach(new SpockExtensionContext(specInfo, store, invocation));
            }
        }

        /**
         * Handle "clean" lifecycle for spock spec
         *
         * @param specInfo SpecInfo of the spock specification target
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of any clean lifecycle methods
         */
        private void onClean(SpecInfo specInfo, IMethodInvocation invocation) throws Throwable {
            if (specInfo != null) {
                // Call the quarkus junit5 extension for all specified clean methods so that all
                // of them are invoked in the classloader context of quarkus
                for (MethodInfo methodInfo : specInfo.getCleanupMethods()) {
                    interceptAfterEachMethod(
                        new SpockInvocation<>(null),
                        new SpockMethodInvocationContext(invocation, methodInfo),
                        new SpockExtensionContext(specInfo, store, invocation)
                    );
                }
                // Make sure that clean methods in super class are also invoked
                onClean(specInfo.getSuperSpec(), invocation);
            }
        }

        /**
         * Handle "clean" lifecycle for current spock spec and its super classes
         *
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of any clean lifecycle methods
         */
        private void onClean(IMethodInvocation invocation) throws Throwable {
            onClean(specInfo, invocation);
        }


        /**
         * Handle "cleanSpec" lifecycle for spock spec
         *
         * @param specInfo SpecInfo of the spock specification target
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of any cleanSpec lifecycle methods
         */
        private void onCleanSpec(SpecInfo specInfo, IMethodInvocation invocation) throws Throwable {
            if (specInfo != null) {
                // Call the quarkus junit5 extension for all specified cleanSpec methods so that all
                // of them are invoked in the classloader context of quarkus
                for (MethodInfo methodInfo : specInfo.getCleanupSpecMethods()) {
                    interceptAfterAllMethod(
                        new SpockInvocation<>(null),
                        new SpockMethodInvocationContext(invocation, methodInfo),
                        new SpockExtensionContext(specInfo, store, invocation)
                    );
                }
                // Make sure that cleanSpec methods in super class are also invoked
                onCleanSpec(specInfo.getSuperSpec(), invocation);
            }
        }

        /**
         * Handle "cleanSpec" lifecycle for current spock spec and its super classes
         *
         * @param invocation Invocation
         * @throws Throwable On exception during invocation of any setup lifecycle methods
         */
        private void onCleanSpec(IMethodInvocation invocation) throws Throwable {
            try {
                onCleanSpec(specInfo, invocation);
                afterAll(new SpockExtensionContext(specInfo, store, invocation));
            } finally {
                // Make sure to close the extension, otherwise ports will be blocked on next
                // spec execution
                QuarkusTestExtensionState state = getState(
                    new SpockExtensionContext(specInfo, store, invocation)
                );
                if (state != null) {
                    state.close();
                }
            }
        }
    }


}
