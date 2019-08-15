package net.mindsoup.dndata.services;

import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.PublishData;

/**
 * Created by Valentijn on 15-8-2019
 */
public interface PublishDataService {

	PublishData getMostRecentPublishDataForBook(Book book);
	PublishData getMostRecentPublishDataForType(ObjectType type);

	void save(PublishData publishData);
}
