package estudo.s.ipsumintegrationtest.assertion;

import java.util.List;

public class SortAssertion {
    
    private String queryParam;

    private List<Integer> sortIndexes;

    public SortAssertion(String queryParam, List<Integer> sortIndexes) {
        this.queryParam = queryParam;
        this.sortIndexes = sortIndexes;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    public List<Integer> getSortIndexes() {
        return sortIndexes;
    }

    public void setSortIndexes(List<Integer> sortIndexes) {
        this.sortIndexes = sortIndexes;
    }

}
