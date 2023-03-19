package estudo.s.integrationtest.account.user;

import static estudo.s.ipsumintegrationtest.constants.HttpStatus.BAD_REQUEST;
import static estudo.s.ipsumintegrationtest.constants.HttpStatus.CREATED;
import static estudo.s.ipsumintegrationtest.constants.HttpStatus.NOT_FOUND;
import static estudo.s.ipsumintegrationtest.constants.HttpStatus.NO_CONTENT;
import static estudo.s.ipsumintegrationtest.constants.HttpStatus.OK;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
// import static io.restassured.module.jsv.JsonSchemaValidator.*;
// import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import estudo.s.ipsumintegrationtest.assertion.IntegrationTest;
import estudo.s.ipsumintegrationtest.assertion.SortAssertion;
import estudo.s.ipsumintegrationtest.assertion.pagination.AssertPagination;
import estudo.s.ipsumintegrationtest.assertion.pagination.Links;
import estudo.s.ipsumintegrationtest.assertion.pagination.Page;
import estudo.s.ipsumintegrationtest.constants.ExpectedMessage;
import estudo.s.ipsumintegrationtest.constants.Message;
import estudo.s.ipsumintegrationtest.utils.QueryParam;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

@TestInstance(Lifecycle.PER_CLASS)
public class UserTestCopy2 extends IntegrationTest {

    private static final String JSON_CONTENT_TYPE = "application/json";

    private static final String EDIT_LINK_PATH = "_links.edit.href";

    private static final String BASE_URL_PROPERTY = "integrationtest.rest.baseurl";
    
    private List<String> deleteLinks = new ArrayList<>();

    private Properties properties = new Properties();

    @BeforeAll
    public void setup() {

        String propertiesPath = propertiesPath();

        try (InputStream input = new FileInputStream(propertiesPath)) {
            
            properties.load(input);
            
        } catch (IOException e) {
            
            e.printStackTrace();

            throw new RuntimeException(e);
        }

    }

    // can be overrided
    private String propertiesPath() {
        return "test.properties";
    }

    @AfterEach
    public void teardown() {
        for (String link : deleteLinks) {
            delete(link);
        }        

        when().
            get(apiUrl()).
        then().
            statusCode(OK.code).
            body("_embedded", nullValue());

        deleteLinks = new ArrayList<>();
    }

    @Test
    public void testInsert() {
        
        JSONObject json = newBody();

        Response response = 
            assertJsonFields(json, 
                given().
                    contentType(JSON_CONTENT_TYPE).
                    body(json.toString()).
                when().
                    post(apiUrl()).
                then().
                    statusCode(CREATED.code).
                    body("id", notNullValue())
            ).
            extract().response();

        String id = response.path("id").toString();
        String responseEditLink = response.path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));

        assertJsonFields(json,
            when().
                get(responseEditLink)
            .then().
                statusCode(OK.code).
                body("id", equalTo(id))
        );

        deleteLinks.add(responseEditLink);
    }

    @Test
    public void testRequiredFieldOnInsert() {

        given().
            contentType(JSON_CONTENT_TYPE).
            body(new JSONObject().toString()).
        when().
            post(apiUrl()).
        then().
            statusCode(BAD_REQUEST.code).
            body("httpStatus", equalTo(BAD_REQUEST.description)).
            body("httpStatusCode", equalTo(BAD_REQUEST.code)).
            body("message", equalTo(new ExpectedMessage(Message.REQUIRED_FIELDS, "User", "\nName\nPassword").toString()));    
    }

    @Test
    public void testFindById() {
        JSONObject json = newBody();

        String id = insert(json);

        String responseEditLink = 
            assertJsonFields(json, 
                when().
                    get(editLink(id)).
                then().
                statusCode(OK.code).
                body("id", equalTo(id))
            ).
            extract().path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));
    }

    @Test 
    public void findByIdWhenNotFound() {

        String id = UUID.randomUUID().toString();
        
        when().
            get(editLink(id)).
        then().
            statusCode(NO_CONTENT.code);
    }

    @Test 
    public void testUpdate() {
        JSONObject json = newBody();

        String id = insert(json);

        json = editBody()
            .put("id", id);

        String responseEditLink = 
            assertJsonFields(json,
                given().
                    contentType(JSON_CONTENT_TYPE).
                    body(json.toString()).
                when().
                    put(editLink(id)).
                then().
                    statusCode(OK.code).
                    body("id", equalTo(id))
            ).
        extract().path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));

        assertJsonFields(json,
            when().
                get(responseEditLink)
            .then().
                statusCode(OK.code).
                body("id", equalTo(id))
        );
    }

    @Test 
    public void testUpdateWhenNotFound() {
        String id = UUID.randomUUID().toString();

        JSONObject json = editBody().put("id", id);

        given().
            contentType(JSON_CONTENT_TYPE).
            body(json.toString()).
        when().
            put(editLink(id)).
        then().
            statusCode(NOT_FOUND.code).
            body("httpStatus", equalTo(NOT_FOUND.description)).
            body("httpStatusCode", equalTo(NOT_FOUND.code)).
            body("message", equalTo(new ExpectedMessage(Message.ENTITY_NOT_FOUND, id).toString()));
    }

    @Test
    public void testUpdateWhenIdFromBodyDiffersFromPathId() {
        JSONObject json = newBody();

        String id = insert(json);

        UUID randomUUID = UUID.randomUUID();

        json = editBody().put("id", randomUUID);

        given().
            contentType(JSON_CONTENT_TYPE).
            body(json.toString()).
        when().
            put(editLink(id)).
        then().
            statusCode(BAD_REQUEST.code).
            body("httpStatus", equalTo(BAD_REQUEST.description)).
            body("httpStatusCode", equalTo(BAD_REQUEST.code)).
            body("message", equalTo(new ExpectedMessage(Message.PATH_ID_DIFFERENT_ENTITY_ID, id, randomUUID).toString()));
    }

    @Test
    public void testUpdateWhenIdFromBodyIsNull() {
        JSONObject json = newBody();

        String id = insert(json);

        UUID emptyId = null;

        json = editBody().put("id", emptyId);

        String responseEditLink = 
            assertJsonFields(json,
                given().
                    contentType(JSON_CONTENT_TYPE).
                    body(json.toString()).
                when().
                    put(editLink(id)).
                then().
                    statusCode(OK.code).
                    body("id", equalTo(id))
            ).
        extract().path(EDIT_LINK_PATH).toString();

        assertThat(responseEditLink).isEqualTo(editLink(id));

        when().
            get(responseEditLink)
        .then().
            statusCode(OK.code).
            body("id", equalTo(id)).
            body("name", equalTo(json.get("name"))).
            body("password", equalTo(json.get("password")));
    }

    @Test
    public void testRequiredFieldOnUpdate() {
        JSONObject json = newBody();

        String id = insert(json);

        JSONObject putJson = new JSONObject()
            .put("id", id);
            
        given().
            contentType(JSON_CONTENT_TYPE).
            body(putJson.toString()).
        when().
            put(editLink(id)).
        then().
            statusCode(BAD_REQUEST.code).
            body("httpStatus", equalTo(BAD_REQUEST.description)).
            body("httpStatusCode", equalTo(BAD_REQUEST.code)).
            body("message", equalTo(new ExpectedMessage(Message.REQUIRED_FIELDS, "User", "\nName\nPassword").toString()));    
    }

    @Test
    public void testDelete() {
        JSONObject json = newBody();

        String id = insert(json);

        when().
            delete(editLink(id)).
        then().
            statusCode(NO_CONTENT.code);
    }

    @Test
    public void testDeleteWhenNotFound() {
        String id = UUID.randomUUID().toString();

        when().
            delete(editLink(id)).
        then().
            statusCode(NOT_FOUND.code).
            body("httpStatus", equalTo(NOT_FOUND.description)).
            body("httpStatusCode", equalTo(NOT_FOUND.code)).
            body("message", equalTo(new ExpectedMessage(Message.ENTITY_NOT_FOUND, id).toString()));   
    }

    @Test
    public void testFindFirstPage() {
        List<JSONObject> jsons = getListBody();

        final int MAX_SIZE = jsons.size();
        
        final int LAST = MAX_SIZE - 1;

        boolean isFirst = true;
        JSONObject firstJson = null;

        for(JSONObject json: jsons) {
            String id = insert(json);
            json.put("id", id);

            if(isFirst) {
                firstJson = json;
            }

            isFirst = false;
        }

        AssertPagination assertPagination = new AssertPagination(
            new Page().
                withSize(1).
                withTotalElements(MAX_SIZE).
                withTotalPages(MAX_SIZE).
                withNumber(0),
                
            new Links().
                withFirst(paginationLink(0, 1)).
                withSelf(paginationLink(0, 1)).
                withNext(paginationLink(1, 1)).
                withLast(paginationLink(LAST, 1)).
                withCreate(apiUrl())
        );

        assertEmbeddedJsonFields(firstJson, 
            assertPagination.validate(
                when().
                    get(paginationLink(0, 1)).
                then().
                    statusCode(OK.code)
            ));
    }

    

    @Test
    public void testFindMidPage() {
        List<JSONObject> jsons = getListBody();

        final int SECOND = 1;
        final int SIZE = jsons.size();
        final int LAST = SIZE - 1;
    
        JSONObject secondJson = null;

        int index = 0;
        for(JSONObject json: jsons) {

            String id = insert(json);
            json.put("id", id);

            if(index == SECOND) {
                secondJson = json;
            }

            index++;
        }

        AssertPagination assertPagination = new AssertPagination(
            new Page()
                .withSize(1)
                .withTotalElements(SIZE)
                .withTotalPages(SIZE)
                .withNumber(1),

            new Links()
                .withFirst(paginationLink(0, 1))
                .withPrev(paginationLink(0, 1))
                .withSelf(paginationLink(1, 1))
                .withNext(paginationLink(2, 1))
                .withLast(paginationLink(LAST, 1))
                .withCreate(apiUrl()));

        assertEmbeddedJsonFields(secondJson, 
            assertPagination.validate(
                when().
                    get(paginationLink(SECOND, 1)).
                then().
                    statusCode(OK.code)
            ));
    }

    @Test
    public void testFindLastPage() {
        List<JSONObject> jsons = getListBody();

        final int SIZE = jsons.size();
        
        final int LAST = SIZE - 1;

        final int SECOND_TO_LAST = LAST - 1;

        JSONObject lastJson = null;

        int index = 0;
        for(JSONObject json: jsons) {

            String id = insert(json);
            json.put("id", id);

            if(index == LAST) {
                lastJson = json;
            }

            index++;
        }

        AssertPagination assertPagination = new AssertPagination(
            new Page()
                .withSize(1)
                .withTotalElements(SIZE)
                .withTotalPages(SIZE)
                .withNumber(LAST), 
            
            new Links()
                .withFirst(paginationLink(0, 1))
                .withPrev(paginationLink(SECOND_TO_LAST, 1))
                .withSelf(paginationLink(LAST, 1))
                .withLast(paginationLink(LAST, 1))
                .withCreate(apiUrl())
        );

        assertEmbeddedJsonFields(lastJson, 
            assertPagination.validate(
                when().
                    get(paginationLink(LAST, 1)).
                then().
                    statusCode(OK.code)
            ));
    }

    @Test
    public void testFindUniquePage() {
        List<JSONObject> jsons = getListBody();

        Map<String, List<Object>> expectedValues = new HashMap<>();

        for(JSONObject json: jsons) {
            String id = insert(json);
            
            if(expectedValues.containsKey("id")) {
                expectedValues.get("id").add(id);

            } else {
                List<Object> values = new ArrayList<>();
                values.add(id);
                expectedValues.put("id", values);
            }

            if(expectedValues.containsKey("editLink")) {
                expectedValues.get("editLink").add(editLink(id));

            } else {
                List<Object> values = new ArrayList<>();
                values.add(editLink(id));
                expectedValues.put("editLink", values);
            }

            for(String field: jsonFieldsToAssert()) {
                if(expectedValues.containsKey(field)) {
                    expectedValues.get(field).add(json.get(field));
    
                } else {
                    List<Object> values = new ArrayList<>();
                    values.add(json.get(field));
                    expectedValues.put(field, values);
                }
            }
        }

        String embeddedPath = embeddedPath();

        final int SIZE = jsons.size();

        AssertPagination assertPagination = new AssertPagination(
            new Page()
                .withSize(SIZE)
                .withTotalElements(SIZE)
                .withTotalPages(1)
                .withNumber(0), 
            
            new Links()
                .withSelf(paginationLink(0, SIZE))
                .withCreate(apiUrl())
        );

        ValidatableResponse assertion = assertPagination.validate(
                when().
                    get(paginationLink(0, SIZE)).
                then().
                    statusCode(OK.code).
                    body("_embedded", notNullValue()).
                    body(embeddedPath, notNullValue()).
                    body(embeddedPath+".id", equalTo(expectedValues.get("id"))).
                    body(embeddedPath+"."+EDIT_LINK_PATH, equalTo(expectedValues.get("editLink")))
            );

        for(String field: jsonFieldsToAssert()) {
            String path = embeddedPath + "." + field;
            assertion.body(path, equalTo(expectedValues.get(field)));
        }
    }

    @Test
    public void testFindEmpty() {

        AssertPagination assertPagination = new AssertPagination(
            new Page()
                .withSize(5)
                .withTotalElements(0)
                .withTotalPages(0)
                .withNumber(0), 
            
            new Links()
                .withSelf(paginationLink(0, 5))
                .withCreate(apiUrl())
        );


        assertPagination.validate(
            when().
                get(apiUrl()).
            then().
                statusCode(OK.code).
                body("_embedded", nullValue()));
            
    }

    @Test 
    public void testFindSorted() {

        List<JSONObject> jsons = getListBody();

            for (JSONObject json: jsons) {
                String id = insert(json);
                json.put("id", id);
            }

            final int MAX_SIZE = jsons.size();

        List<SortAssertion> sortAssertions = sortAssertions();

        for(SortAssertion sortAssertion: sortAssertions) {

            String embeddedPath = embeddedPath();

            ValidatableResponse response = when().
                get(paginationSortLink(0, MAX_SIZE, sortAssertion.getQueryParam())).
            then().
                statusCode(OK.code).
                body("_embedded", notNullValue());
            
        
            for (Integer index: sortAssertion.getSortIndexes()) {
                JSONObject json = jsons.get(index);
                String id = (String) json.get("id");

                assertEmbeddedJsonFields(json, 
                    response.
                        body(embeddedPath, notNullValue()).
                        body(embeddedPath+".id", hasItem(id)).
                        body(embeddedPath+"."+EDIT_LINK_PATH, hasItem(editLink(id)))
                );

            }
                
            AssertPagination assertPagination = new AssertPagination(
                new Page()
                    .withSize(MAX_SIZE)
                    .withTotalElements(MAX_SIZE)
                    .withTotalPages(1)
                    .withNumber(0), 
                
                new Links()
                    .withSelf(paginationSortLink(0, MAX_SIZE, sortAssertion.getQueryParam()))
                    .withCreate(apiUrl())
            );

            assertPagination.validate(response);
        }

        
    }

    // abstract
    private List<SortAssertion> sortAssertions() {
        return List.of(
            new SortAssertion(
                new QueryParam()
                    .put("sort", "name,desc")
                    .toString(), 
                List.of(5, 4, 3, 2, 1, 0)
            ),
            new SortAssertion(
                new QueryParam()
                    .put("sort", "name,asc")
                    .toString(), 
                List.of(0, 1, 2, 3, 4, 5)
            )
        );
    }

    private String embeddedPath() {
        String embeddedDataName = properties.getProperty(embeddedDataNameProperty());
        return "_embedded."+ embeddedDataName;
    }

    // abstract
    private JSONObject newBody() {
        return jsonFromFile("src/test/resources/estudo/s/integrationtest/account/user/newUser.json");
    }

    // abstract
    private JSONObject editBody() {
        return jsonFromFile("src/test/resources/estudo/s/integrationtest/account/user/editUser.json");
    }

    @Override
    public List<JSONObject> listBody() {
        return List.of(
            jsonFromFile("src/test/resources/estudo/s/integrationtest/account/user/user0.json"),
            jsonFromFile("src/test/resources/estudo/s/integrationtest/account/user/user1.json"),
            jsonFromFile("src/test/resources/estudo/s/integrationtest/account/user/user2.json"),
            jsonFromFile("src/test/resources/estudo/s/integrationtest/account/user/user3.json"),
            jsonFromFile("src/test/resources/estudo/s/integrationtest/account/user/user4.json"),
            jsonFromFile("src/test/resources/estudo/s/integrationtest/account/user/user5.json")
        );
    }

    // abstract
    public List<String> jsonFieldsToAssert() {
        return List.of(
            "name",
            "password"
        );
    }

    public ValidatableResponse assertJsonFields(JSONObject json, ValidatableResponse response) {
        ValidatableResponse lastResponse = null;

        for(String field: jsonFieldsToAssert()) {
            lastResponse = response.body(field, equalTo(json.get(field)));
        }

        return lastResponse;
    }

    public ValidatableResponse assertEmbeddedJsonFields(JSONObject json, ValidatableResponse response) {
        ValidatableResponse lastResponse = null;

        String embeddedPath = embeddedPath();

        lastResponse = response.body(embeddedPath, notNullValue());

        for(String field: jsonFieldsToAssert()) {
            String path = embeddedPath + "." + field;
            lastResponse = response.body(path, hasItem(json.get(field)));
        }

        return lastResponse;
    }

    private String insert(JSONObject json) {

        Response response = 
            given().
                contentType(JSON_CONTENT_TYPE).
                body(json.toString()).
            post(apiUrl()).
            then().
                extract().response();

        String id = response.path("id").toString();
        String editLink = response.path(EDIT_LINK_PATH).toString();
            
        deleteLinks.add(editLink);

        return id;
    }

    private JSONObject jsonFromFile(String path) {

        try {
            File file = new File(path);

            if(!file.exists()) {
                throw new RuntimeException(String.format("File not found. Path: %s", path));
            }
            
            InputStream inputStream = new FileInputStream(path);
            String jsonTxt = IOUtils.toString(inputStream, "UTF-8");

            return new JSONObject(jsonTxt);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String editLink(String id) {
        return apiUrl() + "/" + id;
    }

    private String paginationLink(int page, int size) {
        return apiUrl() + "?page=" + page + "&size=" + size;
    }

    private String paginationSortLink(int page, int size, String sortQuery) {
        return paginationLink(page, size) + sortQuery.replace('?', '&');
    }    

    private String apiUrl() {
        String baseUrl = properties.getProperty(BASE_URL_PROPERTY);

        String apiUrl = properties.getProperty(apiUrlProperty());

        return baseUrl + apiUrl;
    }

    // abstract
    private String apiUrlProperty() {
        return "integrationtest.rest.url.user";
    }

    // abstract
    private String embeddedDataNameProperty() {
        return "integrationtest.rest.embeddeddataname.user";
    }

}
