package estudo.s.ipsumintegrationtest.assertion;

import java.util.List;

import org.json.JSONObject;

import estudo.s.ipsumintegrationtest.constants.Message;
import estudo.s.ipsumintegrationtest.exception.IpsumIntegrationTestException;

public abstract class IntegrationTest {

    public List<JSONObject> getListBody() {
        List<JSONObject> listBody = listBody();
        
        validateListBodySize(listBody);

        return listBody;
    }

    private void validateListBodySize(List<JSONObject> listBody) {
        final int MINIMUN_SIZE = 3;

        if(listBody == null || listBody.size() < MINIMUN_SIZE) {
            throw new IpsumIntegrationTestException(Message.INTEGRATION_TEST_LIST_BODY_SIZE_LOWER_THAN_3.message);
        }
    }

    public abstract List<JSONObject> listBody();
    
}
