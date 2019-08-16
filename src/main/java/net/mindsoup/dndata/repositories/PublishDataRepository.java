package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.models.dao.PublishData;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Valentijn on 15-8-2019
 */
public interface PublishDataRepository extends CrudRepository<PublishData, Long> {
	Optional<PublishData> findFirstByBookIdAndGameOrderByIdDesc(Long bookId, Game game);
	Optional<PublishData> findFirstByNameAndGameOrderByIdDesc(String name, Game game);
}
