package estudo.s.ipsumintegrationtest.utils;

public class QueryParam {
    
    String queryParam = "";

    boolean first = true;

    public QueryParam put(String field, String value) {

        String separator = first? "?" : "&";

        String query = separator + field + "=" + value;

        first = false;

        queryParam += query;

        return this;
    }

    @Override
    public String toString() {
        return queryParam;
    }

}
