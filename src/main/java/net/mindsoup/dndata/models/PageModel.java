package net.mindsoup.dndata.models;

import net.mindsoup.dndata.enums.PageType;

/**
 * Created by Valentijn on 7-8-2019
 */
public class PageModel {

	private PageType pageType;

	public PageModel(PageType pageType) {
		this.pageType = pageType;
	}

	public PageType getPageType() {
		return pageType;
	}
}
