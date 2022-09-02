package estudo.s.account.resource.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import estudo.s.account.resource.controller.UserController;
import estudo.s.account.resource.dto.UserDTO;

@Component
public class UserAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>>{


    @Autowired
    private PagedResourcesAssembler<UserDTO> pagedAssembler;

    @Override
    public EntityModel<UserDTO> toModel(UserDTO dto) {
        return EntityModel.of(dto, 
            linkTo(methodOn(UserController.class).findById(dto.getId())).withRel("edit")
        );
    }

    public PagedModel<EntityModel<UserDTO>> toPagedModel(Page<UserDTO> dtos) {
            PagedModel<EntityModel<UserDTO>> pagedModel = pagedAssembler
                .toModel(dtos, this);

            pagedModel.add(linkTo(methodOn(UserController.class).insert(null)).withRel("create"));

            return pagedModel;
    }   
}
