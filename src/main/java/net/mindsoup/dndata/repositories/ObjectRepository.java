package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.dao.DataObject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Valentijn on 3-8-2019
 */
public interface ObjectRepository extends CrudRepository<DataObject, Long> {
	@Query(value = "SELECT * FROM objects WHERE id = :id ORDER BY id, revision DESC LIMIT 1", nativeQuery = true)
	Optional<DataObject> findLastById(@Param("id") Long id);
	Iterable<DataObject> findAllByBookId(Long bookId);

	@Query(value = "SELECT * FROM (SELECT o.id, o.revision, o.json, o.schema_version, o.name, o.type, o.book_id, SUBSTRING_INDEX(GROUP_CONCAT(s.status ORDER BY s.id DESC), ',', 1) as status FROM objects AS o LEFT JOIN object_status AS s ON o.id = s.object_id AND o.revision = s.object_revision GROUP BY s.object_id) AS t WHERE t.book_id = :bookId AND t.status IN :statuses ORDER BY type, name ASC", nativeQuery = true)
	Iterable<DataObject> findAllByBookIdAndStatusIn(@Param("bookId") Long bookId, @Param("statuses") Collection<String> statuses);
}
