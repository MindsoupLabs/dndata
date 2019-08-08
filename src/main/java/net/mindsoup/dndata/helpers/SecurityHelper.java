package net.mindsoup.dndata.helpers;

import net.mindsoup.dndata.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Valentijn on 9-8-2019
 */
public class SecurityHelper {

	public static User getAuthenticatedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(principal == null) {
			return null;
		}

		if(!(principal instanceof User)) {
			return null;
		}

		return (User)principal;
	}

	public static boolean isAuthenticated() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth instanceof UsernamePasswordAuthenticationToken){
			return auth.isAuthenticated();
		}
		return false;
	}

	public static void logout() {
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
	}
}
