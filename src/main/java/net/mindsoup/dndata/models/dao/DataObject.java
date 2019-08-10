package net.mindsoup.dndata.models.dao;

import net.mindsoup.dndata.enums.ObjectType;

import javax.persistence.*;

/**
 * Created by Valentijn on 3-8-2019
 */
@Entity
@Table(name = "objects")
public class DataObject {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	private Integer revision = 1;
	@Column(name = "json")
	private String objectJson = "{}";
	@Column(name = "schema_version")
	private Integer schemaVersion = 1;
	private String name;
	@Enumerated(EnumType.STRING)
	private ObjectType type;
	private Long bookId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getRevision() {
		return revision;
	}

	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public ObjectType getType() {
		return type;
	}

	public void setType(ObjectType type) {
		this.type = type;
	}

	public void setObjectJson(String objectJson) {
		this.objectJson = objectJson;
	}

	public String getObjectJson() {
		return objectJson;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Integer getSchemaVersion() {
		return schemaVersion;
	}

	public void setSchemaVersion(Integer schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
