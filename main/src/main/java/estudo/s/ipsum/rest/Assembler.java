package estudo.s.ipsum.rest;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface Assembler<T> extends RepresentationModelAssembler<T, EntityModel<T>> {
 
    public PagedModel<EntityModel<T>> toPagedModel(Page<T> dtos);

}
