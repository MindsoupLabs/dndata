package net.mindsoup.dndata.models;

import net.mindsoup.dndata.models.dao.Book;

/**
 * Created by Valentijn on 17-8-2019
 */
public class BookProgressModel {
	private Book book;
	private int totalItems;
	private int todoItems;

	public BookProgressModel(Book book, int total, int todo) {
		this.book = book;
		this.todoItems = todo;
		this.totalItems = total;
	}

	public Book getBook() {
		return book;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public int getTodoItems() {
		return todoItems;
	}

	public int getDoneItems() {
		return getTotalItems() - getTodoItems();
	}

	public int getDonePercentage() {
		return Math.round(((float)getDoneItems() / (float)getTotalItems()) * 100);
	}
}
