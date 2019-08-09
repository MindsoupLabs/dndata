package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.UserRight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Valentijn on 8-8-2019
 */
public interface UserRightRepository extends CrudRepository<UserRight, Long> {

	List<UserRight> findByUserId(@Param("user_id") Long userId);
	void deleteByUserId(@Param("user_id") Long userId);
}
