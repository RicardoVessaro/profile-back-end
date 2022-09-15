package estudo.s.ipsum.service;

import static estudo.s.ipsum.exception.ExceptionMessage.COULD_NOT_FOUND_ENTITY_WITH_ID;
import static estudo.s.ipsum.exception.ExceptionMessage.PATH_ID_DIFFERENT_ENTITY_ID;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import estudo.s.ipsum.data.Model;
import estudo.s.ipsum.exception.IpsumException;

public abstract class CRUDServiceImpl<E extends Model<ID>, ID> implements CRUDService<E, ID> {

    @Autowired
    protected ModelMapper modelMapper;

    private boolean validateDeleteInvoked;

    private final JpaRepository<E, ID> repository;

    public CRUDServiceImpl(JpaRepository<E, ID> repository) {
        this.repository = repository;
    }
    
    public abstract RequiredFieldValidator getRequiredFieldValidator(E entity);
    
    @Override
    public E insert(E entity) {
        validateInsert(entity);

        return getRepository().save(entity);
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Override
    public Optional<E> findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public E update(ID id, E entityChanges) {
        if(entityChanges.getId() == null) {
            entityChanges.setId(id);
        }
        
        validateUpdate(id, entityChanges);

        E entity = findByIdOrElseThrow(id);
        
        modelMapper.map(entityChanges, entity);

        return getRepository().save(entity);
    }

    @Override
    public ID delete(ID id) {
        E entity = findByIdOrElseThrow(id);

        validateDelete(id, entity);

        getRepository().delete(entity);

        return id;
    }

    public E findByIdOrElseThrow(ID id) {
        E entity = findById(id)
                .orElseThrow(() -> IpsumException.notFound(COULD_NOT_FOUND_ENTITY_WITH_ID, id));
        return entity;
    }

    public JpaRepository<E, ID> getRepository() {
        return repository;
    }

    public void validateInsert(E entity) {
        validateRequiredFields(entity);
    }

    public void validateUpdate(ID id, E entityChanges) {
        validateId(id, entityChanges);
        
        validateRequiredFields(entityChanges);
    }

    public void validateDelete(ID id, E entity) {
        validateDeleteInvoked = true;
    }

    private void validateId(ID id, E entityChanges) {
        if (!entityChanges.getId().equals(id)) {
            throw new IpsumException(PATH_ID_DIFFERENT_ENTITY_ID, id, entityChanges.getId());
        }
    }

    private void validateRequiredFields(E entity) {
        getRequiredFieldValidator(entity).validate();
    }

    protected boolean isValidateDeleteInvoked() {
        return validateDeleteInvoked;
    }
    
}
