package estudo.s.ipsum.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import estudo.s.ipsum.data.Model;

public interface CRUDService <E extends Model<ID>, ID> {

    E insert(E entity);

    Page<E> findAll(Pageable pageable);

    Optional<E> findById(ID id);

    E update(ID id, E entityChanges);

    ID delete(ID id);
    
}
