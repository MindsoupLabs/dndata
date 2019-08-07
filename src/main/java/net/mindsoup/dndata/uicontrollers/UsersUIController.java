package net.mindsoup.dndata.uicontrollers;

import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.models.PageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/users")
public class UsersUIController {

	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		return "users/index";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		return new PageModel(PageType.USERS);
	}
}
