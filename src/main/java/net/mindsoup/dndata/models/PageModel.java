package net.mindsoup.dndata.models;

import net.mindsoup.dndata.enums.PageType;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Valentijn on 7-8-2019
 */
public class PageModel {

	private PageType pageType;
	private User user;

	public PageModel() {
	}

	public PageType getPageType() {
		return pageType;
	}

	public void setPageType(PageType pageType) {
		this.pageType = pageType;
	}

	/**
	 * You can either add in a partial matching right ('edit' will match rights PF2_EDIT as well as PF1_EDIT) to
	 * match across games, or a complete matching right 'pf2_edit' will only match PF2_EDIT
	 */
	public boolean hasRight(String rightSuffix) {
		if(user == null) {
			return false;
		}

		for(GrantedAuthority grantedAuthority : user.getRights()) {
			if(grantedAuthority.getAuthority().toLowerCase().endsWith(rightSuffix.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
