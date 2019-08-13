package net.mindsoup.dndata.controllers.uicontrollers;

import com.google.gson.Gson;
import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.enums.PageType;
import net.mindsoup.dndata.exceptions.JsonValidationException;
import net.mindsoup.dndata.helpers.PathHelper;
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
import org.apache.commons.io.IOUtils;
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
import java.nio.charset.Charset;
import java.util.*;

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
	public String edit(Model model, @PathVariable(value = "id") Long id) throws IOException {
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
		model.addAttribute("overwriteValuesObject", new Gson().toJson(getOverwriteValueObject(dataObject)));
		model.addAttribute("presetValuesObject", new Gson().toJson(getPresetValueObject(dataObject)));

		String formSchemaLocation = PathHelper.getFormSchema(Game.PF2, dataObject.getType(), dataObject.getSchemaVersion());
		String formSchemaContents = IOUtils.toString(getClass().getResourceAsStream(formSchemaLocation), Charset.defaultCharset());
		model.addAttribute("formSchema", formSchemaContents);

		String uiSchemaLocation = PathHelper.getUISchema(Game.PF2, dataObject.getType(), dataObject.getSchemaVersion());
		String uiSchemaContents = IOUtils.toString(getClass().getResourceAsStream(uiSchemaLocation), Charset.defaultCharset());
		model.addAttribute("uiSchema", uiSchemaContents);
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

		try {
			dataObjectService.save(dataObjectUpdate.getDataObject(), dataObjectUpdate.getComment());
		} catch (JsonValidationException e) {
			// todo: pass filled in data and show error message
			return "redirect:/ui/edit/" + dataObjectUpdate.getDataObject().getId();
		}

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

	private Map<String, Object> getOverwriteValueObject(DataObject dataObject) {
		Book book = bookService.getBookById(dataObject.getBookId());

		Map<String, Object> parentObject = new HashMap<>();
		Map<String, Object> metaData = new HashMap<>();
		Map<String, Object> objectData = new HashMap<>();
		objectData.put("id", dataObject.getId());
		objectData.put("version", dataObject.getRevision());
		Map<String, Object> publicationData = new HashMap<>();
		publicationData.put("sourceBook", book.getName());
		publicationData.put("publisher", book.getPublisher());
		metaData.put("objectData", objectData);
		metaData.put("publicationData", publicationData);
		parentObject.put("metaInformation", metaData);

		return parentObject;
	}

	private Map<String, Object> getPresetValueObject(DataObject dataObject) {
		Map<String, Object> parentObject = new HashMap<>();
		parentObject.put("name", dataObject.getName());

		return parentObject;
	}
}
