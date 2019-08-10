package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.dao.ObjectStatusDAO;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Valentijn on 10-8-2019
 */
public interface ObjectStatusRepository extends CrudRepository<ObjectStatusDAO, Long> {
	Iterable<ObjectStatusDAO> findAllByObjectIdAndObjectRevision(Long objectId, Integer objectRevision);
}
