package estudo.s.ipsumintegrationtest.assertion.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;

public class BodyCallTest {

    @Test
    void testEquals() {
       
        assertThat(
            new BodyCall("Test", notNullValue())
                .equals(new BodyCall("Test", notNullValue()))
        ).isTrue();

        assertThat(
            new BodyCall("Test", notNullValue())
                .equals(new BodyCall("Test 2", notNullValue()))
        ).isFalse();

        assertThat(
            new BodyCall("Test 2", nullValue(), "any", "thing")
                .equals(new BodyCall("Test 2", nullValue(), "any", "thing"))
        ).isTrue();

        assertThat(
            new BodyCall("Test 2", nullValue(), "any", "thing")
                .equals(new BodyCall("Test 2", notNullValue(), "any", "thing"))
        ).isFalse();

        assertThat(
            new BodyCall("Test 2", nullValue(), "any", "thing")
                .equals(new BodyCall("Test 2", nullValue(), "other", "thing"))
        ).isFalse();

        assertThat(
            new BodyCall("Test 3", equalTo("Equal Test"))
                .equals(new BodyCall("Test 3", equalTo("Equal Test")))
        ).isTrue();
    }
}
