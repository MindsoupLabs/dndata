package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.models.User;
import net.mindsoup.dndata.repositories.UserRepository;
import net.mindsoup.dndata.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Valentijn on 8-8-2019
 */
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository repository;

	@Autowired
	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public User getUserByEmail(String email) {
		return repository.findByEmail(email).orElse(null);

	}

	@Override
	public User getUserById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public User createUser(User newUser) {
		return repository.save(newUser);
	}

	@Override
	public User updateUser(User changedUser) {
		return repository.save(changedUser);
	}
}
