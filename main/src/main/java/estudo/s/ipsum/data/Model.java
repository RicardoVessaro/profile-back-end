package estudo.s.ipsum.data;

import org.springframework.data.domain.Persistable;

public abstract class Model<ID> implements Persistable<ID> {

    abstract public void setId(ID id);

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object object) {

        if(object == null) {
            return false;
        }

        if(this.getClass() != object.getClass()) {
            return false;
        }

        Model<ID> model = (Model<ID>) object;

        if(this.getId() == null) {
            return false;
        }
         
        return this.getId().equals(model.getId());
    }
    
}
