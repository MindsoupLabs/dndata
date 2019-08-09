package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.models.UserRight;
import net.mindsoup.dndata.repositories.UserRightRepository;
import net.mindsoup.dndata.services.UserRightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Valentijn on 9-8-2019
 */
@Service
public class UserRightsServiceImpl implements UserRightsService {

	private UserRightRepository repository;

	@Autowired
	public UserRightsServiceImpl(UserRightRepository repository) {
		this.repository = repository;
	}

	@Override
	public Iterable<UserRight> saveRights(List<UserRight> rights) {
		return repository.saveAll(rights);
	}

	@Override
	@Transactional
	public void deleteRightsForUser(Long id) {
		repository.deleteByUserId(id);
	}
}
