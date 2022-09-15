package estudo.s.ipsum.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import estudo.s.ipsum.data.Model;
import estudo.s.ipsum.exception.IpsumException;

@SpringBootTest
@Transactional
public abstract class CRUDServiceImplTest <E extends Model<ID>, ID> {

    private E entity;

    public abstract CRUDServiceImpl<E, ID> getService();
    
    public abstract E newEntity();

    public abstract List<E> newEntities();

    public abstract void editEntity();

    public abstract void assertEntityEquals(E actual, E expected);

    public abstract ID differentId();

    public abstract E emptyEntity();

    public abstract List<String> getRequiredFields();

    public E getEntity() {
        return entity;
    }

    public void setEntity(E entity) {
        this.entity = entity;
    }

    @BeforeEach
    public void setup() {
        setEntity(getService().insert(newEntity()));
    }

    @Test
    public void testInsertAndFindById() {
        Optional<E> optionalEntity = getService().findById(getEntity().getId());

        assertThat(optionalEntity.isPresent()).isTrue();

        E expectedEntity = optionalEntity.get();

        assertEntityEquals(getEntity(), expectedEntity);
    }

    @Test
    public void testFindAll() {

        List<ID> expectedIds = new ArrayList<>();

        expectedIds.add(getEntity().getId());
        
        newEntities().stream().forEach(newEntity -> {
            expectedIds.add(getService().insert(newEntity).getId());
        });

        int pageSize = newEntities().size() + 1;

        Page<E> entities = getService().findAll(Pageable.ofSize(pageSize));

		for (E entity: entities) {
            assertThat(entity.getId()).isIn(expectedIds);
        }
    }

    @Test
    public void testUpdate() {

        editEntity();

        getService().update(getEntity().getId(), getEntity());

        E updatedEntity = getService().findById(getEntity().getId()).get();

        assertEntityEquals(getEntity(), updatedEntity);
    }

    @Test
    public void testUpdateSetIdWhenItIsEmptyInEntity() {

        ID id = getEntity().getId();

        getEntity().setId(null);

        E updatedEntity = getService().update(id, getEntity());

        assertThat(id).isEqualTo(updatedEntity.getId());
    }

    @Test
    public void testUpdateThrowsExceptionWhenGivenIdIsDifferentEntityId() {
        
        ID givenId = differentId();

        IpsumException ipsumException = assertThrows(IpsumException.class, () -> {
            getService().update(givenId, getEntity());
        });

        String expectedMessage =  new StringBuilder()
            .append("id '" + givenId +"' ")
            .append("is different of entity ")
            .append("id '" + getEntity().getId() + "'")
            .toString();

        assertThat(ipsumException.getMessage()).isEqualTo(expectedMessage);
        assertThat(ipsumException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

    }
    
    @Test
    public void testUpdateThrowsExceptionWhenCantFindEntity() {
        getEntity().setId(differentId());

        IpsumException ipsumException = assertThrows(IpsumException.class, () -> {
            getService().update(getEntity().getId(), getEntity());
        });

        String expectedMessage = "Could not find Entity with id '" + getEntity().getId() + "'";

        assertThat(ipsumException.getMessage()).isEqualTo(expectedMessage);
        assertThat(ipsumException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDelete() {

        getService().delete(getEntity().getId());

        Optional<E> optionalEntity = getService().findById(getEntity().getId());

        assertThat(optionalEntity.isEmpty()).isTrue();

        assertThat(getService().isValidateDeleteInvoked()).isTrue();
    }

    @Test
    public void testDeleteThrowsExceptionWhenCantFindEntity() {
        getEntity().setId(differentId());

        IpsumException ipsumException = assertThrows(IpsumException.class, () -> {
            getService().delete(getEntity().getId());
        });

        String expectedMessage = "Could not find Entity with id '" + getEntity().getId() + "'";

        assertThat(ipsumException.getMessage()).isEqualTo(expectedMessage);
        assertThat(ipsumException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testRequiredFieldsOnInsert() {

        E entity = emptyEntity();

        IpsumException ipsumException = assertThrows(IpsumException.class, () -> {
            getService().insert(entity);
        });

        String expectedMessage = getRequiredFieldsMessage();

        assertThat(ipsumException.getMessage()).isEqualTo(expectedMessage);
        assertThat(ipsumException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testRequiredFieldsOnUpdate() {

        E entityChanges = emptyEntity();
        entityChanges.setId(getEntity().getId());

        IpsumException ipsumException = assertThrows(IpsumException.class, () -> {
            getService().update(getEntity().getId(), entityChanges);
        });

        String expectedMessage = getRequiredFieldsMessage();

        assertThat(ipsumException.getMessage()).isEqualTo(expectedMessage);
        assertThat(ipsumException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private String getRequiredFieldsMessage() {
        String expectedMessage = "The following fields are required for User: ";        

        for(String field: getRequiredFields()) {
            expectedMessage += "\n"+field;
        };
        return expectedMessage;
    }

}
