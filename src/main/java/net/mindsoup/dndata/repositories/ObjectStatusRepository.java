package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.dao.ObjectStatusDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Valentijn on 10-8-2019
 */
public interface ObjectStatusRepository extends CrudRepository<ObjectStatusDAO, Long> {
	Iterable<ObjectStatusDAO> findAllByObjectIdAndObjectRevision(Long objectId, Integer objectRevision);
	Iterable<ObjectStatusDAO> findAllByObjectId(Integer objectId);

	@Query(value = "SELECT s, u FROM ObjectStatusDAO s JOIN User u ON s.editorId = u.id WHERE s.objectId = :objectId ORDER BY s.date DESC, s.id DESC")
	Iterable<Object[]> findAllStatusesWithNameByObjectId(@Param("objectId") Integer objectId);
}
