package net.mindsoup.dndata.services;

import net.mindsoup.dndata.models.dao.User;

/**
 * Created by Valentijn on 11-8-2019
 */
public interface ClaimService {
	User getClaimOn(Long objectId);
	boolean claim(Long objectId, User user);
	void clearClaimForUser(Long userId);
	void clearOldClaims();
}
