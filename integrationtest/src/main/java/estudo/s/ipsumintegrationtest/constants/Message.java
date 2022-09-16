package estudo.s.ipsumintegrationtest.constants;

public enum Message {

    REQUIRED_FIELDS("The following fields are required for %s: %s"),
    ENTITY_NOT_FOUND("Could not find Entity with id '%s'"),
    PATH_ID_DIFFERENT_ENTITY_ID("id '%s' is different of entity id '%s'")
    ;
    
    public final String message;

    private Message(String message) {
        this.message = message;
    }

}
