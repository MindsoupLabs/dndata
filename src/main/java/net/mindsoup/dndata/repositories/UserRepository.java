package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by Valentijn on 8-8-2019
 */
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(@Param("email") String email);
}
