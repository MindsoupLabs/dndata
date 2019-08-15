package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.PublishData;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.PublishDataService;
import net.mindsoup.dndata.services.PublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Valentijn on 16-8-2019
 */
@Service
public class PublishingServiceImpl implements PublishingService {

	private PublishDataService publishDataService;
	private DataObjectService dataObjectService;

	@Autowired
	public PublishingServiceImpl(PublishDataService publishDataService, DataObjectService dataObjectService) {
		this.publishDataService = publishDataService;
		this.dataObjectService = dataObjectService;
	}

	@Override
	public void publish(Book book) {

	}

	@Override
	public List<DataObject> getPublishableDataForBook(Book book) {
		PublishData publishData = publishDataService.getMostRecentPublishDataForBook(book);
		Date updatedSince = new Date(0);

		if(publishData != null) {
			updatedSince = publishData.getPublishedDate();
		}

		List<DataObject> objects = dataObjectService.getObjectsReadyForPublishingForBook(book.getId(), updatedSince);
		return filterOutDuplicates(objects);

	}

	private List<DataObject> filterOutDuplicates(List<DataObject> dataObjects) {
		Map<Integer, DataObject> objectsMap = new HashMap<>();

		dataObjects.forEach(d -> {
			if(!objectsMap.containsKey(d.getId()) || objectsMap.get(d.getId()).getRevision() < d.getRevision()) {
				objectsMap.put(d.getId(), d);
			}
		});

		return new LinkedList<>(objectsMap.values());
	}
}
