package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.BookWithObjects;
import net.mindsoup.dndata.models.DataObjectUpdate;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.BookService;
import net.mindsoup.dndata.services.ClaimService;
import net.mindsoup.dndata.services.DataObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/edit")
@ApiIgnore
public class EditUIController extends BaseUIController {

	private BookService bookService;
	private DataObjectService dataObjectService;
	private ClaimService claimService;

	@Autowired
	public EditUIController(BookService bookService,
							DataObjectService dataObjectService,
							ClaimService claimService) {
		this.bookService = bookService;
		this.dataObjectService = dataObjectService;
		this.claimService = claimService;
	}

	@Secured({Constants.Rights.PF2.EDIT})
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		claimService.clearOldClaims();
		Iterable<Book> books = bookService.getAllBooks();

		List<BookWithObjects> booksWithObjects = new LinkedList<>();
		for(Book book : books) {
			BookWithObjects bookWithObjects = new BookWithObjects();
			bookWithObjects.setBook(book);
			List<DataObject> dataObjects = new LinkedList<>();
			dataObjectService.getAllForBookAndStatuses(book.getId(), Arrays.asList(ObjectStatus.CREATED, ObjectStatus.EDITING)).forEach(dataObjects::add);
			bookWithObjects.setObjects(dataObjects);
			if(bookWithObjects.getObjects().size() > 0) {
				booksWithObjects.add(bookWithObjects);
			}
		}

		model.addAttribute("booksWithObjects", booksWithObjects);

		return "edit/index";
	}

	@Secured({Constants.Rights.PF2.EDIT})
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public String edit(Model model, @PathVariable(value = "id") Long id) {
		DataObject dataObject = dataObjectService.getForId(id);
		if(dataObject == null) {
			return "redirect:/ui/edit";
		}
		User me = SecurityHelper.getAuthenticatedUser();
		User user = claimService.getClaimOn(id);
		model.addAttribute("isClaimed", user != null && user.getId() != me.getId());
		model.addAttribute("claimingUser", user);
		claimService.claim(id, me);

		model.addAttribute("statusesWithNames", dataObjectService.getAllStatusesWithNamesForObject(dataObject));
		model.addAttribute("dataObject", dataObject);
		return "edit/detail";
	}

	@Secured({Constants.Rights.PF2.EDIT})
	@RequestMapping(value = {"/update"}, method = RequestMethod.POST)
	public String update(DataObjectUpdate dataObjectUpdate) {
		if(dataObjectService.getForId(dataObjectUpdate.getDataObject().getId()) == null) {
			return "redirect:/ui/edit";
		}
		User me = SecurityHelper.getAuthenticatedUser();
		User claimUser = claimService.getClaimOn(dataObjectUpdate.getDataObject().getId());

		if(claimUser == null || me.getId() != claimUser.getId()) {
			return "redirect:/ui/edit";
		}

		claimService.clearClaimForUser(me.getId());

		dataObjectService.save(dataObjectUpdate.getDataObject(), dataObjectUpdate.getComment());

		if(dataObjectUpdate.isReadyForReview()) {
			dataObjectService.updateStatus(dataObjectUpdate.getDataObject(), Constants.Comments.AUTO_COMMENT_PREFIX + Constants.Comments.READY_FOR_REVIEW, ObjectStatus.AWAITING_REVIEW);
		}

		return "redirect:/ui/edit";
	}

	@Override
	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.EDIT);
		return pageModel;
	}
}
