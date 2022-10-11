package estudo.s.ipsumintegrationtest.constants;

public enum Message {

    REQUIRED_FIELDS("The following fields are required for %s: %s"),
    ENTITY_NOT_FOUND("Could not find Entity with id '%s'"),
    PATH_ID_DIFFERENT_ENTITY_ID("id '%s' is different of entity id '%s'"),
    INTEGRATION_TEST_LIST_BODY_SIZE_LOWER_THAN_3("The method 'listBody' must return at least 3 items.")
    ;
    
    public final String message;

    private Message(String message) {
        this.message = message;
    }

}
