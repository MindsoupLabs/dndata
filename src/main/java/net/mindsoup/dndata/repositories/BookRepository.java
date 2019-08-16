package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.models.dao.Book;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Valentijn on 10-8-2019
 */
public interface BookRepository extends CrudRepository<Book, Long> {
	Iterable<Book> findByGame(Game game);
}
