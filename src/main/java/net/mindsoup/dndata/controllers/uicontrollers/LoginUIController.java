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
@RequestMapping("/ui/")
@ApiIgnore
public class LoginUIController {

	@RequestMapping(value = {"login", "/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		return "login";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		return new PageModel();
	}
}
