package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.dao.UserRight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Valentijn on 8-8-2019
 */
public interface UserRightRepository extends CrudRepository<UserRight, Long> {

	List<UserRight> findByUserId(Long userId);
	void deleteByUserId(Long userId);
}
