package net.mindsoup.dndata.models.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Valentijn on 15-8-2019
 */
@Entity
@Table(name = "publish_data")
public class PublishData {
	@Id
	private Long id;
	private String name;
	private Long bookId;
	private Integer revision;
	private Date publishedDate;

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

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}
}
