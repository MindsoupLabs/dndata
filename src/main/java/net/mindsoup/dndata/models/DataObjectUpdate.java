package net.mindsoup.dndata.models;

import net.mindsoup.dndata.models.dao.DataObject;

/**
 * Created by Valentijn on 11-8-2019
 */
public class DataObjectUpdate {

	private DataObject dataObject;
	private String comment;
	private boolean readyForReview;

	public DataObject getDataObject() {
		return dataObject;
	}

	public void setDataObject(DataObject dataObject) {
		this.dataObject = dataObject;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isReadyForReview() {
		return readyForReview;
	}

	public void setReadyForReview(boolean readyForReview) {
		this.readyForReview = readyForReview;
	}
}
