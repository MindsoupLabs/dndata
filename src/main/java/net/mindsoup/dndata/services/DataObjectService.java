package net.mindsoup.dndata.services;

import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.ObjectStatusDAO;

/**
 * Created by Valentijn on 3-8-2019
 */
public interface DataObjectService {
	DataObject save(DataObject data, String comment);
	DataObject updateStatus(DataObject dataObject, String comment, ObjectStatus status);
	Iterable<DataObject> getAllForBook(Long bookId);
	Iterable<ObjectStatusDAO> getAllStatusesForObject(DataObject dataObject);
}
