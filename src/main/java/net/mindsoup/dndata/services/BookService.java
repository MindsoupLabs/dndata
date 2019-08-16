package net.mindsoup.dndata.services;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.models.dao.Book;

/**
 * Created by Valentijn on 10-8-2019
 */
public interface BookService {
	Iterable<Book> getAllBooks();
	Iterable<Book> getAllBooksByGame(Game game);
	Book getBookById(Long id);
	Book save(Book book);
}
