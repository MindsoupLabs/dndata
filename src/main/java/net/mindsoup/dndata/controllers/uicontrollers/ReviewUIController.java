package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.BookWithObjects;
import net.mindsoup.dndata.models.ReviewResult;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.ObjectStatusDAO;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.BookService;
import net.mindsoup.dndata.services.DataObjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/review")
@ApiIgnore
public class ReviewUIController extends BaseUIController {

	private BookService bookService;
	private DataObjectService dataObjectService;

	@Autowired
	public ReviewUIController(BookService bookService, DataObjectService dataObjectService) {
		this.bookService = bookService;
		this.dataObjectService = dataObjectService;
	}

	@Secured({Constants.Rights.PF2.REVIEW})
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		Iterable<Book> books = bookService.getAllBooks();

		List<BookWithObjects> booksWithObjects = new LinkedList<>();
		for(Book book : books) {
			BookWithObjects bookWithObjects = new BookWithObjects();
			bookWithObjects.setBook(book);
			List<DataObject> dataObjects = new LinkedList<>();
			dataObjectService.getAllForBookAndStatuses(book.getId(), Collections.singletonList(ObjectStatus.AWAITING_REVIEW)).forEach(dataObjects::add);
			bookWithObjects.setObjects(dataObjects);
			if(!bookWithObjects.getObjects().isEmpty()) {
				booksWithObjects.add(bookWithObjects);
			}
		}

		model.addAttribute("booksWithObjects", booksWithObjects);

		return "review/index";
	}

	@Secured({Constants.Rights.PF2.REVIEW})
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public String edit(Model model, @PathVariable(value = "id") Long id) throws IOException {
		ObjectStatusDAO objectStatus = dataObjectService.getStatusById(id);

		if(objectStatus == null) {
			return "redirect:/ui/review";
		}
		DataObject dataObject = dataObjectService.getForIdAndRevision(objectStatus.getObjectId(), objectStatus.getObjectRevision());
		if(dataObject == null) {
			return "redirect:/ui/review";
		}

		model.addAttribute("dataObject", dataObject);
		model.addAttribute("objectStatus", objectStatus);

		return "review/detail";
	}

	@Secured({Constants.Rights.PF2.REVIEW})
	@RequestMapping(value = {"/update"}, method = RequestMethod.POST)
	public String update(ReviewResult reviewResult) {
		ObjectStatusDAO objectStatus = dataObjectService.getStatusById(reviewResult.getStatusId());
		DataObject dataObject = dataObjectService.getForIdAndRevision(reviewResult.getId(), reviewResult.getRevision());
		User me = SecurityHelper.getAuthenticatedUser();

		if(dataObject == null || me == null || objectStatus == null) {
			return "redirect:/ui/review";
		}


		// make sure the status is AWAITING_REVIEW
		// make sure we didn't edit it last ourselves
		if(objectStatus.getStatus() != ObjectStatus.AWAITING_REVIEW || objectStatus.getEditorId().equals(me.getId())) {
			return "redirect:/ui/review";
		}

		// change status
		switch (reviewResult.getAction()) {
			case "reject":
				if(StringUtils.isBlank(reviewResult.getComment())) {
					return String.format("redirect:/ui/review/%s", objectStatus.getId());
				}
				dataObjectService.updateStatus(dataObject, reviewResult.getComment(), ObjectStatus.EDITING);
				break;
			case "approve":
				dataObjectService.updateStatus(dataObject, Constants.Comments.AUTO_COMMENT_PREFIX + Constants.Comments.REVIEW_APPROVED, ObjectStatus.REVIEWED);
				break;
		}
		return "redirect:/ui/review";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.REVIEW);
		return pageModel;
	}
}
