package net.mindsoup.dndata.models.pagemodel;

import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.helpers.RightsHelper;
import net.mindsoup.dndata.models.dao.User;

/**
 * Created by Valentijn on 7-8-2019
 */
public class PageModel {

	private final String version;
	private PageType pageType;
	private User user;

	public PageModel(String version) {
		this.version = version;
	}

	public PageModel(String version, PageType pageType) {
		this(version);
		this.pageType = pageType;
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
		return RightsHelper.hasRight(user, rightSuffix);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getVersion() {
		return version;
	}
}
