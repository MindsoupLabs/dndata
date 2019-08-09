package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/books")
@ApiIgnore
public class BooksUIController extends BaseUIController {

	private BookService bookService;

	@Autowired
	public BooksUIController(BookService bookService) {
		this.bookService = bookService;
	}

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
		model.addAttribute("book", new Book());
		model.addAttribute("games", Game.values());
		return "books/detail";
	}

	@Secured({Constants.Rights.PF2.BOOKS})
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public String edit(Model model, @PathVariable(value = "id") Long id) {
		model.addAttribute("book", bookService.getBookById(id));
		model.addAttribute("games", Game.values());
		return "books/detail";
	}

	@Override
	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.BOOKS);
		return pageModel;
	}
}
