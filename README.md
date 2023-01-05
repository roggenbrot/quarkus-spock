# Quarkus Spock extension

Currently using `@QuarkusTest` in a spock test case is not supported by `Quarkus` [#6506](https://github.com/quarkusio/quarkus/issues/6506)

This is caused by Spock using its own extension mechanism, and not handle JUnit5 annotations out of the box.

There are projects which aim to provide a compatibility/wrapper layer (e.g. `spock-junit5`), but since these projects currently do not 
support interceptors (which the spock JUnit5 extension heavily relies on) , these libraries won't help to excute quarkus test case
with spock.


This extension tries to solve this issue by registering a custom spock extension (`IAnnotationDrivenExtension`) which delegates all spock lifecycle methods
to the corresponding JUnit5 method interceptor handler in the quarkus Junit5 extension


## Usage

Import maven dependency

```
<dependency>
    <groupId>io.quarkiverse.spock</groupId>
    <artifactId>quarkus-spock</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```


```groovy
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
    
}
```

```groovy
@Dependent
@QuarkusSpockTest
class SimpleQuarkusSpockExtensionTest extends Specification {

    @PackageScope
    @Inject
    MockTestService mockTestService
    
    def "Assert that a quarkus test case with parameters can be executed"() {
        given: "Any value"
        def value = "quarkus-spock"
        and: "Mocked InjectTestService"
        QuarkusMock.installMockForType(Mock(InjectTestService) {
            1 * getSuffix(suffix) >> " rocks even more"
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
        "rocks"    || "quarkus-spock rocks even more"
        "is great" || "quarkus-spock rocks even more"
    }
    
}
```
