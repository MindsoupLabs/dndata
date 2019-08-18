package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.configuration.DnDataConfiguration;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/")
@ApiIgnore
public class LoginUIController {

	@Autowired
	private DnDataConfiguration configuration;

	@RequestMapping(value = {"login", "/", ""}, method = RequestMethod.GET)
	public String login() {
		if(SecurityHelper.isAuthenticated()) {
			return "redirect:/ui/index";
		}
		return "login";
	}

	@RequestMapping(value = {"logout"}, method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/ui/login";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		return new PageModel(configuration.getVersion());
	}
}
