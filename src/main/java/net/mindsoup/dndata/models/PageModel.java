package net.mindsoup.dndata.models;

import net.mindsoup.dndata.enums.PageType;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Valentijn on 7-8-2019
 */
public class PageModel {

	private PageType pageType;
	private Set<GrantedAuthority> rights = new HashSet<>();

	public PageModel() {
	}

	public PageType getPageType() {
		return pageType;
	}

	public void setPageType(PageType pageType) {
		this.pageType = pageType;
	}

	public Set<GrantedAuthority> getRights() {
		return rights;
	}

	/**
	 * You can either add in a partial matching right ('edit' will match rights PF2_EDIT as well as PF1_EDIT) to
	 * match across games, or a complete matching right 'pf2_edit' will only  match PF2_EDIT
	 */
	public boolean hasRight(String rightSuffix) {
		for(GrantedAuthority grantedAuthority : rights) {
			if(grantedAuthority.getAuthority().endsWith(rightSuffix)) {
				return true;
			}
		}

		return false;
	}
}
