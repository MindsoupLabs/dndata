package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.domain.ErrorResponse;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Valentijn on 8-8-2019
 */
public abstract class BaseUIController {

	private static Log logger = LogFactory.getLog(BaseUIController.class);

	public abstract PageModel getPageModel();
	public abstract String index(Model model);

	PageModel getBasePageModel() {
		PageModel pageModel = new PageModel();
		if(SecurityHelper.isAuthenticated()) {
			pageModel.setUser(SecurityHelper.getAuthenticatedUser());
		}
		return pageModel;
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception e, Model model) {
		logger.error(String.format("Error %s bubbled up: %s", e.getClass().toString(), e.getMessage()));

		model.addAttribute("errorMessage", e.getMessage());
		model.addAttribute("pageModel", new PageModel(PageType.ERROR));
		return "error/index";
	}
}
