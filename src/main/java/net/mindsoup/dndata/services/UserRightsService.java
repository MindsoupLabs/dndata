package net.mindsoup.dndata.services;

import net.mindsoup.dndata.models.UserRight;

import java.util.List;

/**
 * Created by Valentijn on 8-8-2019
 */
public interface UserRightsService {
	Iterable<UserRight> saveRights(List<UserRight> rights);
	void deleteRightsForUser(Long id);
}
