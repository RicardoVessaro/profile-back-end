package estudo.s.ipsum.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import estudo.s.ipsum.data.Model;
import estudo.s.ipsum.service.CRUDService;


@SpringBootTest
public abstract class CRUDControllerImplTest<E extends Model<ID>, D extends DTO<ID>, ID>  {

    public abstract CRUDControllerImpl<E, D, ID> getController();

    public abstract CRUDService<E, ID> getService();

    public abstract D newDTO();

    public abstract E newEntity();

    public abstract List<E> newEntities();

    public abstract ID differentId();

    @Test
    public void testInsert() {
        
        D dto = newDTO();
        
        E entity = newEntity();

        when(getService().insert(any())).thenReturn(entity);

        ResponseEntity<EntityModel<D>> response = getController().insert(dto);

        assertThat(editLink(entity.getId())).isIn(response.getBody().getLinks());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testFindAll() {
        
        Page<E> entities = new PageImpl<>(newEntities());

        int pageSize = newEntities().size();

        Pageable pageable = Pageable.ofSize(pageSize);
        
        when(getService().findAll(pageable)).thenReturn(entities);

        ResponseEntity<PagedModel<EntityModel<D>>> response = getController().findAll(pageable);

        Link insertLink = linkTo(methodOn(getController().getClass()).insert(null)).withRel("create");
        assertThat(insertLink).isIn(response.getBody().getLinks());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        for(EntityModel<D> item: response.getBody().getContent()) {
            assertThat(editLink(item.getContent().getId())).isIn(item.getLinks());
        }
    }

    @Test
    public void testFindById() {

        E entity = newEntity();

        when(getService().findById(entity.getId())).thenReturn(Optional.of(entity));

        ResponseEntity<EntityModel<D>> response = getController().findById(entity.getId());

        assertThat(response.getBody().getContent().getId()).isEqualTo(entity.getId());

        assertThat(editLink(entity.getId())).isIn(response.getBody().getLinks());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFindByIdMustReturnHttpStatusNoContentWhenNotFound() {

        when(getService().findById(any())).thenReturn(Optional.empty());

        ResponseEntity<EntityModel<D>> response = getController().findById(differentId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void testUpdate() {

        D dto = newDTO();

        E entity = newEntity();
        entity.setId(dto.getId());
        
        when(getService().update(eq(entity.getId()), any())).thenReturn(entity);

        ResponseEntity<EntityModel<D>> response = getController().update(dto.getId(), dto);

        assertThat(editLink(entity.getId())).isIn(response.getBody().getLinks());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(dto.getId()).isEqualTo(entity.getId());
    }

    @Test
    public void testDelete() {
        
        ResponseEntity<EntityModel<D>> response = getController().delete(differentId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    public Link editLink(ID id) {
        return linkTo(methodOn(getController().getClass()).findById(id)).withRel("edit");
    }

}
