package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.pagemodel.PageModel;

/**
 * Created by Valentijn on 8-8-2019
 */
public abstract class BaseUIController {

	public abstract PageModel getPageModel();

	PageModel getBasePageModel() {
		PageModel pageModel = new PageModel();
		if(SecurityHelper.isAuthenticated()) {
			pageModel.setUser(SecurityHelper.getAuthenticatedUser());
		}
		return pageModel;
	}
}
