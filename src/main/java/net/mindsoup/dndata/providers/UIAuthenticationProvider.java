package net.mindsoup.dndata.providers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.models.UserRight;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Valentijn on 7-8-2019
 */
@Component
public class UIAuthenticationProvider implements AuthenticationProvider {
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Collection<UserRight> rights = new HashSet<>();
		rights.add(new UserRight(Constants.Rights.PF2.EDIT));
		rights.add(new UserRight(Constants.Rights.PF2.REVIEW));
		rights.add(new UserRight(Constants.Rights.PF2.PUBLISH));
		rights.add(new UserRight(Constants.Rights.PF2.MANAGE_USERS));
		return new UsernamePasswordAuthenticationToken("name", "password", rights);
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}
}
