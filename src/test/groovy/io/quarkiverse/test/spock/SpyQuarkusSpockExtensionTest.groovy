package io.quarkiverse.test.spock

import io.quarkus.test.junit.QuarkusMock
import spock.lang.Specification

import javax.enterprise.context.Dependent
import javax.inject.Inject

@Dependent
@QuarkusSpockTest
class SpyQuarkusSpockExtensionTest extends Specification {

    @Inject
    MockTestService mockTestService

    def "Assert that a quarkus test case with mocked bean can be executed"() {
        given: "Any value"
        def value = "quarkus-spock"
        and: "Mocked InjectTestService"
        QuarkusMock.installMockForType(Spy(InjectTestService) {
            1 * getSuffix()
            0 * _
        }, InjectTestService.class)
        when: "Any operation is performed"
        def result = value + mockTestService.getSuffix()
        then: "No exception is thrown"
        noExceptionThrown()
        and: "Result has expected value"
        result == "quarkus-spock rocks"
    }
    
    def "Assert that a quarkus test case with parameters can be executed"() {
        given: "Any value"
        def value = "quarkus-spock"
        and: "Mocked InjectTestService"
        QuarkusMock.installMockForType(Spy(InjectTestService) {
            1 * getSuffix(suffix)
            0 * _
        }, InjectTestService.class)
        when: "Any operation is performed"
        def result = value + mockTestService.getSuffix(suffix)
        then: "No exception is thrown"
        noExceptionThrown()
        and: "Result has expected value"
        result == expectedResult
        where:
        suffix     || expectedResult
        "rocks"    || "quarkus-spock rocks"
        "is great" || "quarkus-spock is great"
    }

    def "Assert that a quarkus test case with typed parameters can be executed"(String suffix, String expectedResult) {
        given: "Any value"
        def value = "quarkus-spock"
        and: "Mocked InjectTestService"
        QuarkusMock.installMockForType(Spy(InjectTestService) {
            1 * getSuffix(suffix)
            0 * _
        }, InjectTestService.class)
        when: "Any operation is performed"
        def result = value + mockTestService.getSuffix(suffix)
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
