package estudo.s.ipsumintegrationtest.assertion.pagination;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;

import io.restassured.response.ValidatableResponse;

public class ValidatableResponseStub extends ValidatableResponseNotImpl{
    
    List<BodyCall> bodyCalls = new ArrayList<>();

    @Override
    public ValidatableResponse body(String path, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        
        bodyCalls.add(new BodyCall(path, matcher, additionalKeyMatcherPairs));

        return this;
    }

    public ValidatableResponseStub() {}

    public List<BodyCall> getBodyCalls() {
        return bodyCalls;
    }

}
