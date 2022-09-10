package estudo.s.ipsum.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public abstract class Assembler<D extends DTO<ID>, ID> implements RepresentationModelAssembler<D, EntityModel<D>> {
 
    @Autowired
    private PagedResourcesAssembler<D> pagedAssembler;

    public PagedModel<EntityModel<D>> toPagedModel(Page<D> dtos) {
            PagedModel<EntityModel<D>> pagedModel = pagedAssembler.toModel(dtos, this);

            pagedModel.add(linkTo(methodOn(controllerClass()).insert(null)).withRel("create"));

            return pagedModel;
    }   

    public EntityModel<D> toModel(D dto) {
        return EntityModel.of(dto, 
            linkTo(methodOn(controllerClass()).findById(dto.getId())).withRel("edit")
        );
    }

    public abstract Class<? extends Controller<D, ID>> controllerClass();
    
}
