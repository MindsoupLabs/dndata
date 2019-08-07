package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.models.PageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/index")
@ApiIgnore
public class DashboardUIController extends BaseUIController {

	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		return "dashboard";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.DASHBOARD);
		return pageModel;
	}
}
