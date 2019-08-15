package net.mindsoup.dndata.models;

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
}
