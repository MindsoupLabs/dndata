package net.mindsoup.dndata.providers;

import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Valentijn on 7-8-2019
 */
@Component
public class UIAuthenticationProvider implements AuthenticationProvider {

	private static Log logger = LogFactory.getLog(UIAuthenticationProvider.class);
	private final UserService userService;
	private Map<String, LoginAttempt> loginAttempts = new HashMap<>();

	@Autowired
	public UIAuthenticationProvider(UserService userService) {
		this.userService = userService;
	}

	// TODO: write login/tarpitting unit tests
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = DigestUtils.sha256Hex(authentication.getCredentials().toString());

		if(StringUtils.isAnyBlank(email, password)) {
			logger.warn("Email or password blank");
			throw new UsernameNotFoundException("Email or password blank");
		}

		User user = userService.getUserByEmail(email);

		if(user == null || !user.isActive()) {
			// user does not exist or is not active, reject login
			logger.warn(String.format("User %s not found", email));
			throw new UsernameNotFoundException(String.format("Email %s does not exist", email));
		}

		if(loginAttempts.containsKey(email)) {
			// check this user isn't currently tarpitted for trying too many times
			if(loginAttempts.get(email).getRetryDate().after(new Date())) {
				logger.warn(String.format("User %s login denied due to tarpitting", email));
				// reject login
				throw new BadCredentialsException("Too soon!");
			}
		}

		if(!user.getPassword().equalsIgnoreCase(password)) {
			// if this user's password is incorrect
			// add or up the tarpitting
			int attempts = 0;
			if(loginAttempts.containsKey(email)) {
				attempts = loginAttempts.get(email).getAttempt();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.SECOND, (int)Math.pow(3, attempts));
			loginAttempts.put(email, new LoginAttempt( attempts + 1, c.getTime()));

			logger.warn(String.format("User %s login denied due to invalid password, %s attempts", email, attempts + 1));
			// reject login
			throw new BadCredentialsException("Password incorrect");
		}

		// correct login, remove from failed attempts
		loginAttempts.remove(email);
		logger.info(String.format("User %s logged in", email));
		return new UsernamePasswordAuthenticationToken(user, user.getEmail(), user.getRoles());
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}

	private class LoginAttempt {
		private final int attempt;
		private final Date retryDate;

		LoginAttempt(int attempt, Date retryDate) {
			this.attempt = attempt;
			this.retryDate = retryDate;
		}

		int getAttempt() {
			return attempt;
		}

		Date getRetryDate() {
			return retryDate;
		}
	}
}
