package net.mindsoup.dndata.models;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.models.dao.ObjectStatusDAO;
import net.mindsoup.dndata.models.dao.User;

/**
 * Created by Valentijn on 11-8-2019
 */
public class ObjectStatusWithUser {

	private User user;
	private ObjectStatusDAO objectStatus;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ObjectStatusDAO getObjectStatus() {
		return objectStatus;
	}

	public void setObjectStatus(ObjectStatusDAO objectStatus) {
		this.objectStatus = objectStatus;
	}

	public String getColorClass() {
		switch (getObjectStatus().getStatus()) {
			case CREATED:
				return Constants.Status.Color.CREATED;
			case EDITING:
				return Constants.Status.Color.EDITING;
			case AWAITING_REVIEW:
				return Constants.Status.Color.AWAITING_REVIEW;
			case REVIEWED:
				return Constants.Status.Color.REVIEWED;
			case PUBLISHED:
				return Constants.Status.Color.PUBLISHED;
			case DELETED:
				return Constants.Status.Color.DELETED;
		}

		return "";
	}
	
	public String getIcon() {
		switch (getObjectStatus().getStatus()) {
			case CREATED:
				return Constants.Status.Icon.CREATED;
			case EDITING:
				return Constants.Status.Icon.EDITING;
			case AWAITING_REVIEW:
				return Constants.Status.Icon.AWAITING_REVIEW;
			case REVIEWED:
				return Constants.Status.Icon.REVIEWED;
			case PUBLISHED:
				return Constants.Status.Icon.PUBLISHED;
			case DELETED:
				return Constants.Status.Icon.DELETED;
		}
		
		return "";
	}
}
