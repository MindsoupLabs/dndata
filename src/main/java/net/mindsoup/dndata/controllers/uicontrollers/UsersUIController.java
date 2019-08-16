package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.models.dao.UserRight;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/users")
@ApiIgnore
public class UsersUIController extends BaseUIController {

	private UserService userService;

	@Autowired
	public UsersUIController(UserService userService) {
		this.userService = userService;
	}

	@Override
	@Secured({Constants.Rights.PF2.MANAGE_USERS})
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("userList", userService.getAllUsers());
		return "users/index";
	}

	@Secured({Constants.Rights.PF2.MANAGE_USERS})
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public String edit(Model model, @PathVariable(value = "id") Long id) {
		model.addAttribute("user", userService.getUserById(id));
		model.addAttribute("roles", getAwardableRoles());
		return "users/detail";
	}

	@Secured({Constants.Rights.PF2.MANAGE_USERS})
	@RequestMapping(value = {"/new"}, method = RequestMethod.GET)
	public String newUser(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("roles", getAwardableRoles());
		return "users/detail";
	}

	@Secured({Constants.Rights.PF2.MANAGE_USERS})
	@RequestMapping(value = {"/update"}, method = RequestMethod.POST)
	public String updateUser(User user) {
		user = prepareUser(user);

		userService.updateUser(user);

		return "redirect:/ui/users";
	}

	@Secured({Constants.Rights.PF2.MANAGE_USERS})
	@RequestMapping(value = {"/create"}, method = RequestMethod.POST)
	public String createUser(User user) {
		user = prepareUser(user);

		userService.createUser(user);

		return "redirect:/ui/users";
	}

	@Override
	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.USERS);
		return pageModel;
	}

	private List<String> getAwardableRoles() {
		User me = SecurityHelper.getAuthenticatedUser();

		if(me == null || me.getRoles() == null) {
			return new LinkedList<>();
		}

		return me.getRoles().stream().map(UserRight::getRole).sorted().collect(Collectors.toList());
	}

	private User prepareUser(User user) {
		// assume 64 character strings are already hashed
		if(user.getPassword() != null && user.getPassword().length() != 64) {
			user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
		}

		if(user.getRights() != null) {
			user.setRoles(user.getRights().stream().map(r -> new UserRight(r, user.getId())).collect(Collectors.toList()));
		}

		return user;
	}
}
