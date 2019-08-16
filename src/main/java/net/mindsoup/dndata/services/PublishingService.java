package net.mindsoup.dndata.services;

import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Valentijn on 15-8-2019
 */
public interface PublishingService {
	void publish(Book book) throws IOException, URISyntaxException;
	List<DataObject> getUnpublishedDataForBook(Book book);
}
