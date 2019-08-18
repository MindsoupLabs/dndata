package net.mindsoup.dndata.controllers.restcontrollers;

import com.github.slugify.Slugify;
import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.configuration.DnDataConfiguration;
import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.helpers.PathHelper;
import net.mindsoup.dndata.models.CollectionStatus;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.PublishData;
import net.mindsoup.dndata.services.BookService;
import net.mindsoup.dndata.services.PublishDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Valentijn on 3-8-2019
 */
@RestController
@RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class StatusController extends ErrorController {

	@Autowired
	private PublishDataService publishDataService;

	@Autowired
	private BookService bookService;

	@Autowired
	private DnDataConfiguration dnDataConfiguration;

	private Slugify slugify = new Slugify();

	@RequestMapping("/{game}")
	public Map<String, List<CollectionStatus>> status(@PathVariable(value = "game") Game game) {
		Iterable<Book> books = bookService.getAllBooksByGame(game);
		Map<String, List<CollectionStatus>> collection = new HashMap<>();
		List<CollectionStatus> bookData = new LinkedList<>();
		List<CollectionStatus> collectionsData = new LinkedList<>();
		collection.put(Constants.Collections.BOOKS, bookData);
		collection.put(Constants.Collections.TYPES, collectionsData);

		// add book data
		books.forEach(b -> addIfNotNull(bookData, publishDataService.getMostRecentPublishDataForBook(b)));

		// add collections
		Arrays.stream(ObjectType.values()).forEach(t -> addIfNotNull(collectionsData, publishDataService.getMostRecentPublishDataForName(game, slugify.slugify(t.name()))));
		addIfNotNull(collectionsData, publishDataService.getMostRecentPublishDataForName(game, Constants.Collections.COLLECTION_ALL));

		return collection;
	}

	@RequestMapping("/test")
	public boolean test(@RequestParam(value = "path") String path) throws IOException {
		InputStream inputStream = StatusController.class.getResourceAsStream(path);
		inputStream.read();
		return true;
	}

	private void addIfNotNull(List<CollectionStatus> dataList, PublishData data) {
		if(data != null) {
			dataList.add(CollectionStatus.fromPublishData(data, dnDataConfiguration.getDownloadBaseUrl()));
		}
	}
}
