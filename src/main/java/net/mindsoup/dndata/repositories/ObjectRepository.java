package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.dao.DataObject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Valentijn on 3-8-2019
 */
public interface ObjectRepository extends CrudRepository<DataObject, Long> {
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO objects (id, revision, json, schema_version, name, type, book_id) VALUES (:id, :revision, :json, :schema_version, :name, :type, :book_id)", nativeQuery = true)
	void saveInsert(@Param("id") Long id, @Param("revision") Integer revision, @Param("json") String json, @Param("schema_version") Integer schema_version, @Param("name") String name, @Param("type") String type, @Param("book_id") Long book_id);

	@Query(value = "SELECT * FROM objects WHERE id = :id ORDER BY id, revision DESC LIMIT 1", nativeQuery = true)
	Optional<DataObject> findLastById(@Param("id") Long id);

	@Query(value = "SELECT * FROM (SELECT o.id, MAX(o.revision) AS revision, o.json, o.schema_version, o.name, o.type, o.book_id, SUBSTRING_INDEX(GROUP_CONCAT(s.status ORDER BY s.id DESC), ',', 1) as status FROM objects AS o LEFT JOIN object_status AS s ON o.id = s.object_id AND o.revision = s.object_revision GROUP BY s.object_id) AS t WHERE t.book_id = :bookId AND t.status IN :statuses ORDER BY type, name ASC", nativeQuery = true)
	Iterable<DataObject> findAllByBookIdAndStatusIn(@Param("bookId") Long bookId, @Param("statuses") Collection<String> statuses);

	Optional<DataObject> findByIdAndRevision(Long id, Integer revision);
}
