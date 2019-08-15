package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.models.BookWithObjects;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.PublishData;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.BookService;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.PublishDataService;
import net.mindsoup.dndata.services.PublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/publish")
@ApiIgnore
public class PublishUIController extends BaseUIController{

	private BookService bookService;
	private PublishingService publishingService;

	@Autowired
	public PublishUIController(BookService bookService, PublishingService publishingService) {
		this.bookService = bookService;
		this.publishingService = publishingService;
	}

	@Secured({Constants.Rights.PF2.PUBLISH})
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		Iterable<Book> books = bookService.getAllBooks();

		List<BookWithObjects> booksWithObjects = new LinkedList<>();
		for(Book book : books) {
			List<DataObject> objects = publishingService.getPublishableDataForBook(book);

			if(!objects.isEmpty()) {
				booksWithObjects.add(new BookWithObjects(book, objects));
			}
		}

		model.addAttribute("booksWithObjects", booksWithObjects);

		return "publish/index";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.PUBLISH);
		return pageModel;
	}
}
