package net.mindsoup.dndata.models.dao;

import net.mindsoup.dndata.enums.Game;

import javax.persistence.*;

/**
 * Created by Valentijn on 10-8-2019
 */
@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String publisher;
	private long isbn;
	@Enumerated(EnumType.STRING)
	private Game game;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public long getIsbn() {
		return isbn;
	}

	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
