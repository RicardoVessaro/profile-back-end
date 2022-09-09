package estudo.s.account.rest.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import estudo.s.account.rest.controller.UserController;
import estudo.s.account.rest.dto.UserDTO;
import estudo.s.ipsum.rest.Assembler;

@Component
public class UserAssembler implements Assembler<UserDTO>{


    @Autowired
    private PagedResourcesAssembler<UserDTO> pagedAssembler;

    @Override
    public EntityModel<UserDTO> toModel(UserDTO dto) {
        return EntityModel.of(dto, 
            linkTo(methodOn(UserController.class).findById(dto.getId())).withRel("edit")
        );
    }

    @Override
    public PagedModel<EntityModel<UserDTO>> toPagedModel(Page<UserDTO> dtos) {
            PagedModel<EntityModel<UserDTO>> pagedModel = pagedAssembler.toModel(dtos, this);

            pagedModel.add(linkTo(methodOn(UserController.class).insert(null)).withRel("create"));

            return pagedModel;
    }   
}
