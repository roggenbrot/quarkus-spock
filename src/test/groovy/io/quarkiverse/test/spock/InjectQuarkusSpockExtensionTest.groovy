package io.quarkiverse.test.spock

import spock.lang.Specification

import javax.inject.Inject

@QuarkusSpockTest
class InjectQuarkusSpockExtensionTest extends Specification {

    @Inject
    InjectTestService injectTestService

    def "Assert that a quarkus test case with injected bean can be executed"() {
        given: "Any value"
        def value = "quarkus-spock"
        when: "Any operation is performed"
        def result = value + injectTestService.getSuffix()
        then: "No exception is thrown"
        noExceptionThrown()
        and: "Result has expected value"
        result == "quarkus-spock rocks"
    }

    def "Assert that a quarkus test case with parameters and injected bean can be executed"() {
        given: "Any value"
        def value = "quarkus-spock"
        when: "Any operation is performed"
        def result = value + injectTestService.getSuffix(suffix)
        then: "No exception is thrown"
        noExceptionThrown()
        and: "Result has expected value"
        result == expectedResult
        where:
        suffix     || expectedResult
        "rocks"    || "quarkus-spock rocks"
        "is great" || "quarkus-spock is great"
    }

    def "Assert that a quarkus test case with typed parameters and injected bean can be executed"(String suffix, String expectedResult) {
        given: "Any value"
        def value = "quarkus-spock"
        when: "Any operation is performed"
        def result = value + injectTestService.getSuffix(suffix)
        then: "No exception is thrown"
        noExceptionThrown()
        and: "Result has expected value"
        result == expectedResult
        where:
        suffix     || expectedResult
        "rocks"    || "quarkus-spock rocks"
        "is great" || "quarkus-spock is great"
    }


}
