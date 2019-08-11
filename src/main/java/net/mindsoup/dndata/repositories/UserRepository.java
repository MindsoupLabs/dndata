package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.dao.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;

/**
 * Created by Valentijn on 8-8-2019
 */
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);


	Optional<User> findByClaimObjectId(Long objectId);
	@Modifying
	@Query(value = "UPDATE User SET claimDate = :date, claimObjectId = :objectId WHERE id = :userId")
	void claimObject(Long userId, Long objectId, Date date);
	@Modifying
	@Query(value = "UPDATE User SET claimDate = NULL, claimObjectId = NULL WHERE claimDate < :expireDate")
	void deleteExpiredClaims(Date expireDate);
	@Modifying
	@Query(value = "UPDATE User SET claimDate = NULL, claimObjectId = NULL WHERE id = :userId")
	void deleteClaimForUser(Long userId);

}
