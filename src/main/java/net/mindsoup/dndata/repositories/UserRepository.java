package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.dao.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Valentijn on 8-8-2019
 */
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
