package estudo.s.ipsum.rest;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.common.reflect.TypeToken;

import estudo.s.ipsum.data.Model;
import estudo.s.ipsum.service.CRUDService;

public abstract class CRUDControllerImpl<E extends Model<ID>, D extends DTO<ID>, ID> implements CRUDController<D, ID> {

    private CRUDService<E, ID> service;

    private Assembler<D, ID> assembler;

    private Class<E> entityClass;

    @Autowired
    private ModelMapper modelMapper;
    
    @SuppressWarnings("unchecked")
    public CRUDControllerImpl(CRUDService<E, ID> service, Assembler<D, ID> assembler) {
        this.service = service;
        this.assembler = assembler;
        
        entityClass = (Class<E>) new TypeToken<E>(getClass()) {}.getType();
    }

    protected abstract ResponseBuilder<E, D, ID> responseBuilder();

    @Override
    public ResponseEntity<EntityModel<D>> insert(D body) {
        E entity = getModelMapper().map(body, entityClass);

        entity = getService().insert(entity);

        return responseBuilder().createResponse(entity, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PagedModel<EntityModel<D>>> findAll(Pageable pageable) {
        Page<E> entities = getService().findAll(pageable);

        return responseBuilder().createResponse(entities);
    }

    @Override
    public ResponseEntity<EntityModel<D>> findById(ID id) {
        Optional<E> optionalEntity = getService().findById(id);

        if (optionalEntity.isPresent()) {
            return responseBuilder().createResponse(optionalEntity.get());
        }

        return responseBuilder().noContent();
    }

    @Override
    public ResponseEntity<EntityModel<D>> update(ID id, D body) {
        E entityChanges = getModelMapper().map(body, entityClass);

        E entity = getService().update(id, entityChanges);

        return responseBuilder().createResponse(entity);
    }

    @Override
    public ResponseEntity<EntityModel<D>> delete(ID id) {
        getService().delete(id);

        return responseBuilder().noContent();
    }

    protected CRUDService<E, ID> getService() {
        return service;
    }

    protected Assembler<D, ID> getAssembler() {
        return assembler;
    }

    protected ModelMapper getModelMapper() {
        return modelMapper;
    }     

}
