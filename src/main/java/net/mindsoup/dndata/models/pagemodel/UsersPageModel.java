package net.mindsoup.dndata.models.pagemodel;

import net.mindsoup.dndata.models.dao.User;

/**
 * Created by Valentijn on 9-8-2019
 */
public class UsersPageModel extends PageModel {

	private Iterable<User> users;

	public UsersPageModel(String version) {
		super(version);
	}

	public Iterable<User> getUsers() {
		return users;
	}

	public void setUsers(Iterable<User> users) {
		this.users = users;
	}
}
