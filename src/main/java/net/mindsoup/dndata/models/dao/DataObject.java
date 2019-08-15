package net.mindsoup.dndata.models.dao;

import net.mindsoup.dndata.enums.ObjectType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Valentijn on 3-8-2019
 */
@Entity
@Table(name = "objects")
public class DataObject {
	@Id
	@Type(type = "uuid-char")
	@GeneratedValue( generator = "uuid2" )
	@GenericGenerator( name = "uuid2", strategy = "uuid2" )
	@Column(name = "internal_id")
	private UUID internalId;
	private Integer id;
	private Integer revision;
	@Column(name = "json")
	private String objectJson = "{}";
	@Column(name = "schema_version")
	private Integer schemaVersion = 1;
	private String name;
	@Enumerated(EnumType.STRING)
	private ObjectType type;
	private Long bookId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public UUID getInternalId() {
		return internalId;
	}

	public void setInternalId(UUID internalId) {
		this.internalId = internalId;
	}
}
