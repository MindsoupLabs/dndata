package net.mindsoup.dndata.models;

import net.mindsoup.dndata.enums.Right;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Valentijn on 7-8-2019
 */
public class UserRight implements GrantedAuthority {

	private Right right;

	public UserRight(Right right) {
		this.right = right;
	}

	@Override
	public String getAuthority() {
		return right.name();
	}

	public Right getRight() {
		return right;
	}
}
