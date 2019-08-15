package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.helpers.PathHelper;
import net.mindsoup.dndata.models.ValidationResult;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.PublishData;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.JsonValidatorService;
import net.mindsoup.dndata.services.PublishDataService;
import net.mindsoup.dndata.services.PublishingService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Valentijn on 16-8-2019
 */
@Service
public class PublishingServiceImpl implements PublishingService {

	private static Log logger = LogFactory.getLog(PublishingServiceImpl.class);

	private PublishDataService publishDataService;
	private DataObjectService dataObjectService;
	private JsonValidatorService jsonValidatorService;

	@Autowired
	public PublishingServiceImpl(PublishDataService publishDataService, DataObjectService dataObjectService, JsonValidatorService jsonValidatorService) {
		this.publishDataService = publishDataService;
		this.dataObjectService = dataObjectService;
		this.jsonValidatorService = jsonValidatorService;
	}

	@Override
	public void publish(Book book) {
		logger.info(String.format("Publishing changes in book %s: %s", book.getId(), book.getName()));
		List<DataObject> objectsInThisBook = getUnpublishedDataForBook(book);

		validateObjects(book, objectsInThisBook);

		Set<ObjectType> publishingTypes = new HashSet<>();
		objectsInThisBook.forEach(o -> publishingTypes.add(o.getType()));

		publishBook(book);
		publishingTypes.forEach(this::publishType);
		publishAll();
		logger.info("Publishing completed");
	}

	@Override
	public List<DataObject> getUnpublishedDataForBook(Book book) {
		Date updatedSince = getLastPublishDate(publishDataService.getMostRecentPublishDataForBook(book));
		return dataObjectService.getUnpublishedObjectsForBook(book.getId(), updatedSince);
	}

	private Date getLastPublishDate(PublishData publishData) {
		Date updatedSince = new Date(0);

		if(publishData != null) {
			updatedSince = publishData.getPublishedDate();
		}

		return updatedSince;
	}

	private void validateObjects(Book book, List<DataObject> dataObjects) {
		for(DataObject dataObject : dataObjects) {
			ValidationResult validationResult =	jsonValidatorService.validate(dataObject.getObjectJson(), PathHelper.getSchema(book.getGame(), dataObject.getType(), dataObject.getSchemaVersion()));
			if(!validationResult.isValid()) {
				throw validationResult.getValidationException();
			}
		}
	}

	private void publishBook(Book book) {
		logger.info(String.format("Publishing book %s: %s", book.getId(), book.getName()));
		// TODO:
		// get map of JSON data (json file content
		createObjectMapFromList(dataObjectService.getAllPublishableObjectsForBook(book.getId()));
		// convert to file
		// upload to location
		// update publish data
	}

	private void publishType(ObjectType type) {
		logger.info(String.format("Publishing collection %s", type.name()));
		createObjectMapFromList(dataObjectService.getAllPublishableObjectsForType(type));
	}

	private void publishAll() {
		logger.info("Publishing collection ALL");
		createObjectMapFromList(dataObjectService.getAllPublishableObjects());
	}

	private Map<ObjectType, List<JSONObject>> createObjectMapFromList(List<DataObject> dataObjects) {
		Map<ObjectType,  List<JSONObject>> map = new HashMap<>();
		dataObjects.forEach(d -> {
			if(!map.containsKey(d.getType())) {
				map.put(d.getType(), new LinkedList<>());
			}
			map.get(d.getType()).add(new JSONObject(d.getObjectJson()));
		});

		return map;
	}
}
