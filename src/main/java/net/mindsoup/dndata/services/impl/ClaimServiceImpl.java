package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.repositories.UserRepository;
import net.mindsoup.dndata.services.ClaimService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Valentijn on 11-8-2019
 */
@Service
public class ClaimServiceImpl implements ClaimService {

	private UserRepository userRepository;

	public ClaimServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User getClaimOn(Integer objectId) {
		return userRepository.findByClaimObjectId(objectId).orElse(null);
	}

	@Override
	@Transactional
	public boolean claim(Integer objectId, User user) {
		User previousClaim = getClaimOn(objectId);
		if(previousClaim != null) {
			return false;
		}

		userRepository.claimObject(user.getId(), objectId, new Date());

		return true;
	}

	@Override
	@Transactional
	public void clearClaimForUser(Long userId) {
		userRepository.deleteClaimForUser(userId);
	}

	@Override
	@Transactional
	public void clearOldClaims() {
		userRepository.deleteExpiredClaims(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24)));
	}
}
