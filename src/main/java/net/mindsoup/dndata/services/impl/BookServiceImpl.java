package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.repositories.BookRepository;
import net.mindsoup.dndata.services.BookService;
import org.springframework.stereotype.Service;

/**
 * Created by Valentijn on 10-8-2019
 */
@Service
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;

	public BookServiceImpl(BookRepository repository) {
		this.bookRepository = repository;
	}


	@Override
	public Iterable<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@Override
	public Book getBookById(Long id) {
		return bookRepository.findById(id).orElse(null);
	}

	@Override
	public Book save(Book book) {
		return bookRepository.save(book);
	}
}
