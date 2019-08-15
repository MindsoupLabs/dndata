package net.mindsoup.dndata.models;

import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.ObjectStatusDAO;

/**
 * Created by Valentijn on 15-8-2019
 */
public class DataObjectAndObjectStatus {
	private DataObject dataObject;
	private ObjectStatusDAO objectStatus;

	public DataObjectAndObjectStatus(DataObject object, ObjectStatusDAO status) {
		this.dataObject = object;
		this.objectStatus = status;
	}

	public DataObject getDataObject() {
		return dataObject;
	}

	public ObjectStatusDAO getObjectStatus() {
		return objectStatus;
	}
}
