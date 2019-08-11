package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.ObjectStatusWithName;
import net.mindsoup.dndata.models.dao.ObjectStatusDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Valentijn on 10-8-2019
 */
public interface ObjectStatusRepository extends CrudRepository<ObjectStatusDAO, Long> {
	Iterable<ObjectStatusDAO> findAllByObjectIdAndObjectRevision(Long objectId, Integer objectRevision);
	Iterable<ObjectStatusDAO> findAllByObjectId(Long objectId);

	@Query(value = "SELECT s.id, s.status, s.comment, s.editor_id, u.name, s.object_id, s.object_revision, s.status_date FROM object_status AS s JOIN users AS u ON s.editor_id = u.id WHERE s.object_id = :objectId ORDER BY s.status_date, s.id DESC", nativeQuery = true)
	Iterable<ObjectStatusWithName> findAllStatusesWithNameByObjectId(@Param("objectId") Long objectId);
}
