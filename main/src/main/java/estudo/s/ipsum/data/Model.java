package estudo.s.ipsum.data;

import org.springframework.data.domain.Persistable;

public interface Model<ID> extends Persistable<ID> {

    void setId(ID id);

}
