package estudo.s.account.rest.dto;

import java.util.UUID;

import estudo.s.ipsum.rest.DTO;

public class UserDTO implements DTO<UUID> {

    private UUID id;
    private String name;
    private String password;

    public UserDTO() {}

    @Override
    public UUID getId() {
        return id;
    }

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
}
