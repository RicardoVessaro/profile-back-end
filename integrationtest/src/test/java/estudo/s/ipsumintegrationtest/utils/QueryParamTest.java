package estudo.s.ipsumintegrationtest.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class QueryParamTest {

    @Test
    public void testQueryParam() {

        String queryParam = new QueryParam()
            .put("name", "foo")
            .put("number", "1")
        .toString();

        String expectedQueryParam = "?name=foo&number=1";

        assertThat(queryParam).isEqualTo(expectedQueryParam);
    }

    @Test
    public void testQueryParamEmpty() {

        String queryParam = new QueryParam()
            .toString();

        String expectedQueryParam = "";

        assertThat(queryParam).isEqualTo(expectedQueryParam);
    }

}
