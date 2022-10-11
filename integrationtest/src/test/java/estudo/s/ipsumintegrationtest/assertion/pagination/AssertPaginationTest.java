package estudo.s.ipsumintegrationtest.assertion.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

public class AssertPaginationTest {

    @Test
    void testValidate() {

        ValidatableResponseStub validatableResponse = new ValidatableResponseStub();

        Links links = new Links()
            .withFirst("first")
            .withPrev("prev")
            .withSelf("self")
            .withNext("next")
            .withLast("last")
            .withCreate("create");

        Page page = new Page()
            .withSize(1)
            .withTotalElements(2)
            .withNumber(3)
            .withTotalPages(4);

        AssertPagination assertPagination = new AssertPagination(page, links);

        assertPagination.validate(validatableResponse);

        assertThat(new BodyCall(AssertPagination.LINKS, notNullValue())).isIn(validatableResponse.getBodyCalls());
        
        assertThat(new BodyCall(AssertPagination.LINKS_FIRST, notNullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_FIRST_HREF, equalTo(links.getFirst()))).isIn(validatableResponse.getBodyCalls());

        assertThat(new BodyCall(AssertPagination.LINKS_PREV, notNullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_PREV_HREF, equalTo(links.getPrev()))).isIn(validatableResponse.getBodyCalls());

        assertThat(new BodyCall(AssertPagination.LINKS_SELF, notNullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_SELF_HREF, equalTo(links.getSelf()))).isIn(validatableResponse.getBodyCalls());

        assertThat(new BodyCall(AssertPagination.LINKS_NEXT, notNullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_NEXT_HREF, equalTo(links.getNext()))).isIn(validatableResponse.getBodyCalls());

        assertThat(new BodyCall(AssertPagination.LINKS_CREATE, notNullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_CREATE_HREF, equalTo(links.getCreate()))).isIn(validatableResponse.getBodyCalls());



        assertThat(new BodyCall(AssertPagination.PAGE, notNullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE_SIZE, equalTo(page.getSize()))).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE_TOTAL_ELEMENTS, equalTo(page.getTotalElements()))).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE_TOTAL_PAGES, equalTo(page.getTotalPages()))).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE_NUMBER, equalTo(page.getNumber()))).isIn(validatableResponse.getBodyCalls());

    }

    @Test
    public void testValidateWithNullPageAndNullLinks() {

        AssertPagination assertPagination = new AssertPagination(null, null);

        ValidatableResponseStub validatableResponse = new ValidatableResponseStub();

        assertPagination.validate(validatableResponse);

        assertThat(new BodyCall(AssertPagination.LINKS, nullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE, nullValue())).isIn(validatableResponse.getBodyCalls());

    }

    @Test
    public void testValidateWithEmptyValues() {
        Page page = new Page();
        
        AssertPagination assertPagination = new AssertPagination(page, new Links());

        ValidatableResponseStub validatableResponse = new ValidatableResponseStub();

        assertPagination.validate(validatableResponse);

        assertThat(new BodyCall(AssertPagination.LINKS_FIRST, nullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_PREV, nullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_SELF, nullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_NEXT, nullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.LINKS_CREATE, nullValue())).isIn(validatableResponse.getBodyCalls());

        assertThat(new BodyCall(AssertPagination.PAGE, notNullValue())).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE_SIZE, equalTo(page.getSize()))).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE_TOTAL_ELEMENTS, equalTo(page.getTotalElements()))).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE_TOTAL_PAGES, equalTo(page.getTotalPages()))).isIn(validatableResponse.getBodyCalls());
        assertThat(new BodyCall(AssertPagination.PAGE_NUMBER, equalTo(page.getNumber()))).isIn(validatableResponse.getBodyCalls());

    }
}
