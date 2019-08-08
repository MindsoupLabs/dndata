package net.mindsoup.dndata.models;

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
	private int objectVersion;
	private ObjectType objectType;
	private String objectJson;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getObjectVersion() {
		return objectVersion;
	}

	public void setObjectVersion(int objectVersion) {
		this.objectVersion = objectVersion;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public void setObjectJson(String objectJson) {
		this.objectJson = objectJson;
	}

	public String getObjectJson() {
		return objectJson;
	}
}
