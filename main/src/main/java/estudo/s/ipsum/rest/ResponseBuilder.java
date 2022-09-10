package estudo.s.ipsum.rest;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.reflect.TypeToken;

public abstract class ResponseBuilder<E, D extends DTO<ID>, ID> {

    private ModelMapper modelMapper = new ModelMapper();

    private Class<D> dtoClass;

    private Assembler<D, ID> assembler;

    @SuppressWarnings("unchecked")
    public ResponseBuilder(Assembler<D, ID> assembler) {

        this.assembler = assembler;

        dtoClass = (Class<D>) new TypeToken<D>(getClass()) {}.getType();
    }
    
    public ResponseEntity<EntityModel<D>> createResponse(E entity) {
        return createResponse(entity, HttpStatus.OK);
    }

    public ResponseEntity<EntityModel<D>> createResponse(E entity, HttpStatus httpStatus) {
        D dto = modelMapper.map(entity, dtoClass);

        return new ResponseEntity<>(assembler.toModel(dto), httpStatus);
    }

    public ResponseEntity<PagedModel<EntityModel<D>>> createResponse(Page<E> entities) {
        return createResponse(entities, HttpStatus.OK);
    }

    public ResponseEntity<PagedModel<EntityModel<D>>> createResponse(Page<E> entities, HttpStatus httpStatus) {
        Page<D> dtos = entities.map(entity -> {
            return modelMapper.map(entity, dtoClass);
        });

        return ResponseEntity
            .status(httpStatus)
            .body(assembler.toPagedModel(dtos));
    }

    public ResponseEntity<EntityModel<D>> noContent() {
        return ResponseEntity.noContent().build();
    }

}
