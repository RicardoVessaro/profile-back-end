package estudo.s.ipsum.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface Controller<D extends DTO<ID>, ID> {
    
    ResponseEntity<EntityModel<D>> insert(@RequestBody D body);

    ResponseEntity<EntityModel<D>> findById(@PathVariable ID id);
}
