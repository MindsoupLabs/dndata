package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.exceptions.DnDataException;
import net.mindsoup.dndata.models.User;
import net.mindsoup.dndata.models.UserRight;
import net.mindsoup.dndata.repositories.UserRepository;
import net.mindsoup.dndata.services.UserRightsService;
import net.mindsoup.dndata.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Valentijn on 8-8-2019
 */
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository repository;
	private final UserRightsService userRightsService;

	@Autowired
	public UserServiceImpl(UserRepository repository, UserRightsService userRightsService) {
		this.repository = repository;
		this.userRightsService = userRightsService;
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
		if(StringUtils.isAnyBlank(newUser.getEmail(), newUser.getPassword(), newUser.getName())) {
			throw new DnDataException("User details incomplete");
		}

		// first save the user without rights
		List<UserRight> rights = newUser.getRoles();
		newUser.setRoles(null);
		User createdUser = repository.save(newUser);

		// update the rights with the user id, then save those
		if(rights != null) {
			rights.forEach(r -> r.setUserId(createdUser.getId()));
			Iterable<UserRight> createdRights = userRightsService.saveRights(rights);

			// return user with the updated rights
			List<UserRight> rightsList = new LinkedList<>();
			createdRights.forEach(rightsList::add);
			createdUser.setRoles(new LinkedList<>(rightsList));
		}

		return createdUser;
	}

	@Override
	public User updateUser(User changedUser) {
		userRightsService.deleteRightsForUser(changedUser.getId());
		return createUser(changedUser);
	}

	@Override
	public Iterable<User> getAllUsers() {
		return repository.findAll();
	}
}
