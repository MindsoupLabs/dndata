package net.mindsoup.dndata.models;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Valentijn on 7-8-2019
 */
public class UserRight implements GrantedAuthority {

	private String right;

	public UserRight(String right) {
		this.right = right;
	}

	@Override
	public String getAuthority() {
		return right;
	}

	public String getRight() {
		return right;
	}
}
