package io.quarkiverse.test.spock;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InjectTestService {

    public String getSuffix(){
        return " rocks";
    }

    public String getSuffix(String value){
        return " " + value;
    }
}
