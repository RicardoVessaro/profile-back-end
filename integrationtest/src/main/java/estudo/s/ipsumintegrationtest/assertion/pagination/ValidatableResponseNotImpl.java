package estudo.s.ipsumintegrationtest.assertion.pagination;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matcher;

import io.restassured.http.ContentType;
import io.restassured.matcher.DetailedCookieMatcher;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.parsing.Parser;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.response.ValidatableResponseLogSpec;
import io.restassured.specification.Argument;
import io.restassured.specification.ResponseSpecification;

public class ValidatableResponseNotImpl implements ValidatableResponse{

    @Override
    public ValidatableResponse body(String path, List<Argument> arguments, Matcher matcher,
            Object... additionalKeyMatcherPairs) {

        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse body(List<Argument> arguments, Matcher matcher, Object... additionalKeyMatcherPairs) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse body(List<Argument> arguments, ResponseAwareMatcher<Response> responseAwareMatcher) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse statusCode(Matcher<? super Integer> expectedStatusCode) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse statusCode(int expectedStatusCode) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse statusLine(Matcher<? super String> expectedStatusLine) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse statusLine(String expectedStatusLine) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse headers(Map<String, ?> expectedHeaders) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse headers(String firstExpectedHeaderName, Object firstExpectedHeaderValue,
            Object... expectedHeaders) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse header(String headerName, Matcher<?> expectedValueMatcher) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse header(String headerName, ResponseAwareMatcher<Response> expectedValueMatcher) {
        throw new NotImplementedException();
    }

    @Override
    public <V> ValidatableResponse header(String headerName, Function<String, V> mappingFunction,
            Matcher<? super V> expectedValueMatcher) {
        throw new NotImplementedException();
    }

    @Override
    public ValidatableResponse header(String headerName, String expectedValue) {
        
        return null;
    }

    @Override
    public ValidatableResponse cookies(Map<String, ?> expectedCookies) {
        
        return null;
    }

    @Override
    public ValidatableResponse cookie(String cookieName) {
        
        return null;
    }

    @Override
    public ValidatableResponse cookies(String firstExpectedCookieName, Object firstExpectedCookieValue,
            Object... expectedCookieNameValuePairs) {
        
        return null;
    }

    @Override
    public ValidatableResponse cookie(String cookieName, Matcher<?> expectedValueMatcher) {
        
        return null;
    }

    @Override
    public ValidatableResponse cookie(String cookieName, DetailedCookieMatcher detailedCookieMatcher) {
        
        return null;
    }

    @Override
    public ValidatableResponse cookie(String cookieName, Object expectedValue) {
        
        return null;
    }

    @Override
    public ValidatableResponse rootPath(String rootPath) {
        
        return null;
    }

    @Override
    public ValidatableResponse rootPath(String rootPath, List<Argument> arguments) {
        
        return null;
    }

    @Override
    public ValidatableResponse root(String rootPath, List<Argument> arguments) {
        
        return null;
    }

    @Override
    public ValidatableResponse root(String rootPath) {
        
        return null;
    }

    @Override
    public ValidatableResponse noRoot() {
        
        return null;
    }

    @Override
    public ValidatableResponse noRootPath() {
        
        return null;
    }

    @Override
    public ValidatableResponse appendRootPath(String pathToAppend) {
        
        return null;
    }

    @Override
    public ValidatableResponse appendRootPath(String pathToAppend, List<Argument> arguments) {
        
        return null;
    }

    @Override
    public ValidatableResponse detachRootPath(String pathToDetach) {
        
        return null;
    }

    @Override
    public ValidatableResponse contentType(ContentType contentType) {
        
        return null;
    }

    @Override
    public ValidatableResponse contentType(String contentType) {
        
        return null;
    }

    @Override
    public ValidatableResponse contentType(Matcher<? super String> contentType) {
        
        return null;
    }

    @Override
    public ValidatableResponse body(Matcher<?> matcher, Matcher<?>... additionalMatchers) {
        
        return null;
    }

    @Override
    public ValidatableResponse body(String path, List<Argument> arguments,
            ResponseAwareMatcher<Response> responseAwareMatcher) {
        
        return null;
    }

    @Override
    public ValidatableResponse body(String path, ResponseAwareMatcher<Response> responseAwareMatcher) {
        
        return null;
    }

    @Override
    public ValidatableResponse body(String path, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        
        return null;
    }

    @Override
    public ValidatableResponse and() {
        
        return null;
    }

    @Override
    public ValidatableResponse using() {
        
        return null;
    }

    @Override
    public ValidatableResponse assertThat() {
        
        return null;
    }

    @Override
    public ValidatableResponse spec(ResponseSpecification responseSpecificationToMerge) {
        
        return null;
    }

    @Override
    public ValidatableResponse parser(String contentType, Parser parser) {
        
        return null;
    }

    @Override
    public ValidatableResponse defaultParser(Parser parser) {
        
        return null;
    }

    @Override
    public ExtractableResponse<Response> extract() {
        
        return null;
    }

    @Override
    public ValidatableResponseLogSpec<ValidatableResponse, Response> log() {
        
        return null;
    }

    @Override
    public ValidatableResponse time(Matcher<Long> matcher) {
        
        return null;
    }

    @Override
    public ValidatableResponse time(Matcher<Long> matcher, TimeUnit timeUnit) {
        
        return null;
    }


    
}
