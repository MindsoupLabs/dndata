package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.models.BookWithObjects;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.BookService;
import net.mindsoup.dndata.services.PublishingService;
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
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

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

	@Override
	@Secured({Constants.Rights.PF2.PUBLISH})
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		Iterable<Book> books = bookService.getAllBooks();

		List<BookWithObjects> booksWithObjects = new LinkedList<>();
		for(Book book : books) {
			List<DataObject> objects = publishingService.getUnpublishedDataForBook(book);

			if(!objects.isEmpty()) {
				booksWithObjects.add(new BookWithObjects(book, objects));
			}
		}

		model.addAttribute("booksWithObjects", booksWithObjects);

		return "publish/index";
	}

	@Secured({Constants.Rights.PF2.PUBLISH})
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public String publish(Model model, @PathVariable(value = "id") Long id) throws IOException, URISyntaxException {
		Book book = bookService.getBookById(id);
		publishingService.publish(book);
		return "redirect:/ui/publish/";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.PUBLISH);
		return pageModel;
	}
}
