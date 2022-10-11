package estudo.s.ipsumintegrationtest.assertion.pagination;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;

public class BodyCall {

    private String path;

    private Matcher<?> matcher;

    private Object[] additionalKeyMatcherPairs;

    public BodyCall(String path, Matcher<?> matcher, Object... additionalKeyMatcherPairs) {
        this.path = path;
        this.matcher = matcher;
        this.additionalKeyMatcherPairs = additionalKeyMatcherPairs;
    }

    public boolean isIn(List<BodyCall> bodyCalls) {

        for(BodyCall bodyCall: bodyCalls) {
            if(this.equals(bodyCall)) {
                return true;
            }
        }

        return false;
    }

    public String getPath() {
        return path;
    }

    public Matcher<?> getMatcher() {
        return matcher;
    }

    public Object[] getAdditionalKeyMatcherPairs() {
        return additionalKeyMatcherPairs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((matcher == null) ? 0 : matcher.hashCode());
        result = prime * result + Arrays.deepHashCode(additionalKeyMatcherPairs);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BodyCall other = (BodyCall) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (matcher == null) {
            if (other.matcher != null)
                return false;
        } else if (!matcher.equals(other.matcher) 
                    && !matcher.toString().equals(other.matcher.toString()))
            return false;
        if (!Arrays.deepEquals(additionalKeyMatcherPairs, other.additionalKeyMatcherPairs))
            return false;
        return true;
    }

    

}
