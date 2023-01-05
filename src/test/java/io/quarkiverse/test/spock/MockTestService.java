package io.quarkiverse.test.spock;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MockTestService {


    @Inject
    InjectTestService testService;

    public String getSuffix() {
        return testService.getSuffix();
    }

    public String getSuffix(String value) {
        return testService.getSuffix(value);
    }
}
