package estudo.s.ipsum.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CRUDController<D extends DTO<ID>, ID> {
    
    @PostMapping
    ResponseEntity<EntityModel<D>> insert(@RequestBody D body);

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<D>>> findAll(Pageable pageable);

    @GetMapping("/{id}")
    ResponseEntity<EntityModel<D>> findById(@PathVariable ID id);

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<D>> update(@PathVariable ID id, @RequestBody D body);

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<D>> delete(@PathVariable ID id);

}
