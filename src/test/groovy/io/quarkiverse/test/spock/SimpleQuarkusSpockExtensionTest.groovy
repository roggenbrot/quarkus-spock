package io.quarkiverse.test.spock

import spock.lang.Specification

import javax.enterprise.context.Dependent

@Dependent
@QuarkusSpockTest
class SimpleQuarkusSpockExtensionTest extends Specification {


    def "Assert that a simple quarkus test case can be executed"() {
        given: "Any value"
        def value = "quarkus-spock"
        when: "Any operation is performed"
        def result = value + " rocks"
        then: "No exception is thrown"
        noExceptionThrown()
        and: "Result has expected value"
        result == "quarkus-spock rocks"
    }


    def "Assert that a simple quarkus test case with parameters can be executed"() {
        given: "Any value"
        def value = "quarkus-spock"
        when: "Any operation is performed"
        def result = value + suffix
        then: "No exception is thrown"
        noExceptionThrown()
        and: "Result has expected value"
        result == expectedResult
        where:
        suffix      || expectedResult
        " rocks"    || "quarkus-spock rocks"
        " is great" || "quarkus-spock is great"
    }

    def "Assert that a simple quarkus test case with typed parameters can be executed"(String suffix, String expectedResult) {
        given: "Any value"
        def value = "quarkus-spock"
        when: "Any operation is performed"
        def result = value + suffix
        then: "No exception is thrown"
        noExceptionThrown()
        and: "Result has expected value"
        result == expectedResult
        where:
        suffix      || expectedResult
        " rocks"    || "quarkus-spock rocks"
        " is great" || "quarkus-spock is great"
    }


}
