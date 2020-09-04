package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.BookService;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.impl.DataObjectServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/books")
@ApiIgnore
public class BooksUIController extends BaseUIController {
	private static Log logger = LogFactory.getLog(BooksUIController.class);

	private BookService bookService;
	private DataObjectService dataObjectService;

	@Autowired
	public BooksUIController(BookService bookService, DataObjectService dataObjectService) {
		this.bookService = bookService;
		this.dataObjectService = dataObjectService;
	}

	@Override
	@Secured({Constants.Rights.PF2.BOOKS})
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("books", bookService.getAllBooks());
		return "books/index";
	}

	@Secured({Constants.Rights.PF2.BOOKS})
	@RequestMapping(value = {"/update"}, method = RequestMethod.POST)
	public String updateBook(Book book) {
		bookService.save(book);

		return "redirect:/ui/books";
	}

	@Secured({Constants.Rights.PF2.BOOKS})
	@RequestMapping(value = {"/new"}, method = RequestMethod.GET)
	public String newUser(Model model) {
		model.addAttribute("showCreateObject", false);
		model.addAttribute("book", new Book());
		model.addAttribute("games", Game.values());
		return "books/detail";
	}

	@Secured({Constants.Rights.PF2.BOOKS})
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public String edit(Model model, @PathVariable(value = "id") Long id, @RequestParam(value = "previousType", required = false) ObjectType previousType) {
		if(previousType == null) {
			previousType = ObjectType.CREATURE;
		}

		model.addAttribute("book", bookService.getBookById(id));
		model.addAttribute("games", Game.values());
		model.addAttribute("objectTypes", ObjectType.getActiveObjectTypes());
		model.addAttribute("showCreateObject", true);
		model.addAttribute("objects", dataObjectService.getAllForBook(id));
		model.addAttribute("previousType", previousType);
		return "books/detail";
	}

	@Secured({Constants.Rights.PF2.BOOKS})
	@RequestMapping(value = {"/addObject"}, method = RequestMethod.POST)
	public String addObject(DataObject object) {
		dataObjectService.save(object, Constants.Comments.AUTO_COMMENT_PREFIX + Constants.Comments.OBJECT_CREATED);

		return "redirect:/ui/books/" + object.getBookId() + "?previousType=" + object.getType();
	}

	@Secured({Constants.Rights.PF2.BOOKS})
	@RequestMapping(value = {"/addObjects"}, method = RequestMethod.POST)
	public String addObjects(DataObject object) {
		// this task can take a long time so run it in a separate thread
		Runnable backgroundTask = () -> {
			String names = object.getName();
			// split giant string of object names by newlines
			String[] namesArray = names.split("(\r\n|\n|\r)");
			logger.info(String.format("Creating %s new objects of type %s", namesArray.length, object.getType()));
			// for each object, add it
			for(String name : namesArray) {
				name = name.trim();
				DataObject dataObject = new DataObject();
				dataObject.setName(name);
				dataObject.setBookId(object.getBookId());
				dataObject.setType(object.getType());

				addObject(dataObject);
			}
			logger.info(String.format("%s objects created", namesArray.length));
		};

		new Thread(SecurityHelper.wrapSecurityAwareRunnable(backgroundTask)).start();

		return "redirect:/ui/books/" + object.getBookId() + "?previousType=" + object.getType();
	}

	@Override
	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.BOOKS);
		return pageModel;
	}
}
