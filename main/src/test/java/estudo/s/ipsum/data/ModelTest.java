package estudo.s.ipsum.data;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ModelTest {

    @Test
    void testEquals() {
        
        UUID id = UUID.randomUUID();

        assertThat(
            new TestModel(id).equals(new TestModel(id))
        ).isTrue();

        assertThat(
            new TestModel(UUID.randomUUID()).equals(new TestModel(UUID.randomUUID()))
        ).isFalse();

        assertThat(
            new TestModel(UUID.randomUUID()).equals(new TestModel(null))
        ).isFalse();

        assertThat(
            new TestModel(null).equals(new TestModel(UUID.randomUUID()))
        ).isFalse();

        assertThat(
            new TestModel(null).equals(new TestModel(null))
        ).isFalse();

        assertThat(
            new TestModel(id).equals(new OtherModel(id))
        ).isFalse();

        assertThat(
            new TestModel(id).equals(new Object())
        ).isFalse();

        assertThat(
            new TestModel(id).equals(null)
        ).isFalse();

        assertThat(
            new TestModel(id).equals(new DifferentModel(1L))
        ).isFalse();
        
    }

    class TestModel extends Model<UUID> {

        private UUID id;

        public TestModel(UUID id) {
            this.id = id;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return id == null;
        }

        @Override
        public void setId(UUID id) {
            this.id = id;
        }

    }

    class OtherModel extends Model<UUID> {

        private UUID id;

        public OtherModel(UUID id) {
            this.id = id;
        }

        @Override
        public UUID getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return id == null;
        }

        @Override
        public void setId(UUID id) {
            this.id = id;
        }

    }

    class DifferentModel extends Model<Long> {

        private Long id;

        public DifferentModel(Long id) {
            this.id = id;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public boolean isNew() {
            return id == null;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

    }
    
}
