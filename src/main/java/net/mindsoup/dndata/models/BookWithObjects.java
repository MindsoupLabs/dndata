package net.mindsoup.dndata.models;

import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;

import java.util.List;

/**
 * Created by Valentijn on 10-8-2019
 */
public class BookWithObjects {
	private Book book;
	private List<DataObject> objects;

	public BookWithObjects() {
	}

	public BookWithObjects(Book book, List<DataObject> objects) {
		this.book = book;
		this.objects = objects;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public List<DataObject> getObjects() {
		return objects;
	}

	public void setObjects(List<DataObject> objects) {
		this.objects = objects;
	}
}
