package net.mindsoup.dndata.models.dao;

import net.mindsoup.dndata.enums.ObjectStatus;

/**
 * Created by Valentijn on 14-8-2019
 */
public class DataObjectwithStatus extends DataObject {

	private ObjectStatus status;
	private Long statusId;

	public ObjectStatus getStatus() {
		return status;
	}

	public void setStatus(ObjectStatus status) {
		this.status = status;
	}

	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
}
