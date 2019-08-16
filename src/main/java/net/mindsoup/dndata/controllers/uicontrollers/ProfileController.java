package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.exceptions.UserInputException;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Valentijn on 9-8-2019
 */
@Controller
@RequestMapping("/ui/profile")
@ApiIgnore
public class ProfileController extends BaseUIController {

	private final UserService userService;

	public ProfileController(UserService userService) {
		this.userService = userService;
	}

	@Override
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		return "profile/index";
	}

	@RequestMapping(value = {"/updatePassword", ""}, method = RequestMethod.POST)
	public String updatePassword(String password) {
		if(StringUtils.isBlank(password)) {
			throw new UserInputException("Password cannot be blank");
		}

		if(SecurityHelper.isAuthenticated()) {
			User user = SecurityHelper.getAuthenticatedUser();
			user.setPassword(DigestUtils.sha256Hex(password));

			userService.updateUser(user);
		}
		return "redirect:/ui/logout";
	}

	@Override
	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.PROFILE);
		return pageModel;
	}
}
