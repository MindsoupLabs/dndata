package net.mindsoup.dndata.helpers;

import net.mindsoup.dndata.models.dao.User;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Valentijn on 17-8-2019
 */
public class RightsHelper {

	public static boolean hasRight(User user, String suffix) {
		if(user == null || user.getRoles() == null) {
			return false;
		}

		for(GrantedAuthority grantedAuthority : user.getRoles()) {
			if(grantedAuthority.getAuthority().toLowerCase().endsWith(suffix.toLowerCase())) {
				return true;
			}
		}

		return false;
	}
}
