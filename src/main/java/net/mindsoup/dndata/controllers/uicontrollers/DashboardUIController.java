package net.mindsoup.dndata.controllers.uicontrollers;

import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.models.BookProgressModel;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.DataObjectwithStatus;
import net.mindsoup.dndata.models.pagemodel.PageModel;
import net.mindsoup.dndata.services.BookService;
import net.mindsoup.dndata.services.DataObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by Valentijn on 7-8-2019
 */
@Controller
@RequestMapping("/ui/index")
@ApiIgnore
public class DashboardUIController extends BaseUIController {

	private BookService bookService;
	private DataObjectService dataObjectService;

	@Autowired
	public DashboardUIController(BookService bookService, DataObjectService dataObjectService) {
		this.bookService = bookService;
		this.dataObjectService = dataObjectService;
	}

	@Override
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public String index(Model model) {

		List<BookProgressModel> editProgress = new LinkedList<>();
		List<BookProgressModel> reviewProgress = new LinkedList<>();

		Iterable<Book> books = bookService.getAllBooks();

		for(Book book : books) {
			Iterable<DataObjectwithStatus> all = dataObjectService.getAllForBookAndStatuses(book.getId(), Arrays.asList(ObjectStatus.CREATED, ObjectStatus.EDITING, ObjectStatus.AWAITING_REVIEW, ObjectStatus.REVIEWED, ObjectStatus.PUBLISHED));

			int allSize = (int) StreamSupport.stream(all.spliterator(), false).count();
			int editTodo = (int) StreamSupport.stream(all.spliterator(), false).filter(d -> d.getStatus() == ObjectStatus.EDITING || d.getStatus() == ObjectStatus.CREATED).count();
			// everything with a review status or with edit status (since anything to be edited will also need to be reviewed)
			int reviewTodo = (int) StreamSupport.stream(all.spliterator(), false).filter(d -> d.getStatus() == ObjectStatus.AWAITING_REVIEW).count() + editTodo;

			if(editTodo > 0) {
				editProgress.add(new BookProgressModel(book, allSize, editTodo));
			}

			if(reviewTodo > 0) {
				reviewProgress.add(new BookProgressModel(book, allSize, reviewTodo));
			}
		}

		model.addAttribute("editProgressList", editProgress);
		model.addAttribute("reviewProgressList", reviewProgress);

		return "dashboard";
	}

	@ModelAttribute("pageModel")
	public PageModel getPageModel() {
		PageModel pageModel = getBasePageModel();
		pageModel.setPageType(PageType.DASHBOARD);
		return pageModel;
	}
}
