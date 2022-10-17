package estudo.s.ipsumintegrationtest.assertion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import estudo.s.ipsumintegrationtest.constants.Message;
import estudo.s.ipsumintegrationtest.exception.IpsumIntegrationTestException;

public class TestIntegrationTest {
    
    @Test
    public void testGetListBodyThrowExceptionWhenReturnsLessThan3ItemsOnList() {

        IntegrationTest integrationTestImpl = new IntegrationTest() {

            @Override
            public List<JSONObject> listBody() {
                return List.of(new JSONObject());
            }
            
        };

        IpsumIntegrationTestException exception = assertThrows(IpsumIntegrationTestException.class, () -> {
            integrationTestImpl.getListBody();
        });

        assertThat(exception.getMessage()).isEqualTo(Message.INTEGRATION_TEST_LIST_BODY_SIZE_LOWER_THAN_3.message);

    }

    @Test
    public void testGetListBody() {

        JSONObject json0 = new JSONObject();
        JSONObject json1 = new JSONObject();
        JSONObject json2 = new JSONObject();

        IntegrationTest integrationTestImpl = new IntegrationTest() {

            @Override
            public List<JSONObject> listBody() {

                return List.of(json0, json1, json2);
            }
            
        };

        List<JSONObject> listBody = integrationTestImpl.getListBody();

        assertThat(listBody.get(0)).isEqualTo(json0);
        assertThat(listBody.get(1)).isEqualTo(json1);
        assertThat(listBody.get(2)).isEqualTo(json2);

    }

}
