package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.DataObjectwithStatus;
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

	@Query(value = Constants.SQL.GET_ALL_OBJECTS_IN_BOOK_BY_STATUS, nativeQuery = true)
	Iterable<Object[]> findAllByBookIdAndStatusInAsObjectArray(@Param("bookId") Long bookId, @Param("statuses") Collection<String> statuses);

	@Query(value = Constants.SQL.GET_ALL_OBJECTS_IN_BOOK_BY_STATUS, nativeQuery = true)
	Iterable<DataObject> findAllByBookIdAndStatusIn(@Param("bookId") Long bookId, @Param("statuses") Collection<String> statuses);

	@Query(value = "SELECT d FROM DataObject d WHERE id = :id AND revision = :revision")
	Optional<DataObject> findByIdAndRevision(@Param("id") Long id, @Param("revision") Integer revision);
}
