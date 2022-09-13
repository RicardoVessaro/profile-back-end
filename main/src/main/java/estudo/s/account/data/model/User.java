package estudo.s.account.data.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import estudo.s.ipsum.data.Model;

@Entity
@Table(name = "Users")
public class User implements Model<UUID> {

    @Id
    @GenericGenerator(name = "USER_GENERATOR", strategy = "uuid2")
    @GeneratedValue(generator = "USER_GENERATOR")
    private UUID id;

    private String name;

    private String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object object) {
        User user = (User) object;

        if (this.getId() == null && user.getId() == null ) {
            return true;
        
        } else if(this.getId() != null && user.getId() == null) {
            return false;
        }

        return this.getId().equals(user.getId());
    }
}
