package net.mindsoup.dndata.controllers.uicontrollers;

import com.google.gson.Gson;
import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.helpers.PathHelper;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.BookWithObjects;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.BookService;
import net.mindsoup.dndata.services.DataObjectService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
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
			if(bookWithObjects.getObjects().size() > 0) {
				booksWithObjects.add(bookWithObjects);
			}
		}

		model.addAttribute("booksWithObjects", booksWithObjects);

		return "review/index";
	}

	@Secured({Constants.Rights.PF2.EDIT})
	@RequestMapping(value = {"/{id}/{revision}"}, method = RequestMethod.GET)
	public String edit(Model model, @PathVariable(value = "id") Long id, @PathVariable(value = "revision") Integer revision) throws IOException {
		DataObject dataObject = dataObjectService.getForIdAndRevision(id, revision);
		if(dataObject == null) {
			return "redirect:/ui/review";
		}

		model.addAttribute("dataObject", dataObject);

		return "review/detail";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.REVIEW);
		return pageModel;
	}
}
