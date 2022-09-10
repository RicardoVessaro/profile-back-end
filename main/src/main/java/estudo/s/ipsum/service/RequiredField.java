package estudo.s.ipsum.service;

public class RequiredField {
    
    private String getter;

    private String alias;

    public RequiredField(String getter, String alias) {
        this.getter = getter;
        this.alias = alias;
    }

    public String getGetter() {
        return getter;
    }

    public String getAlias() {
        return alias;
    }

}
