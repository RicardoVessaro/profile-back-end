package estudo.s.account.service;

import java.util.UUID;

import estudo.s.account.data.model.User;
import estudo.s.ipsum.service.CRUDService;

public interface UserService extends CRUDService<User, UUID> {

}
