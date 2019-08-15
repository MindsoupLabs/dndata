package net.mindsoup.dndata.services;

import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;

import java.util.List;

/**
 * Created by Valentijn on 15-8-2019
 */
public interface PublishingService {
	void publish(Book book);
	List<DataObject> getUnpublishedDataForBook(Book book);
}
