package net.mindsoup.dndata.services;

import net.mindsoup.dndata.models.User;

/**
 * Created by Valentijn on 8-8-2019
 */
public interface UserService {

	User getUserByEmail(String email);
	User getUserById(Long id);
	User createUser(User newUser);
	User updateUser(User changedUser);
	Iterable<User> getAllUsers();
}
