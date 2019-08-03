package net.mindsoup.dndata.models;

import net.mindsoup.dndata.enums.ObjectType;

/**
 * Created by Valentijn on 3-8-2019
 */
public class DataObject {
	private long id;
	private int objectVersion;
	private ObjectType ObjectType;
	private String objectJson;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getObjectVersion() {
		return objectVersion;
	}

	public void setObjectVersion(int objectVersion) {
		this.objectVersion = objectVersion;
	}

	public ObjectType getObjectType() {
		return ObjectType;
	}

	public void setObjectType(ObjectType objectType) {
		ObjectType = objectType;
	}

	public void setObjectJson(String objectJson) {
		this.objectJson = objectJson;
	}

	public String getObjectJson() {
		return objectJson;
	}
}
