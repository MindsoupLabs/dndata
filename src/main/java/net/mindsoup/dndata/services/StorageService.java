package net.mindsoup.dndata.services;

import net.mindsoup.dndata.models.dao.DataObject;

/**
 * Created by Valentijn on 3-8-2019
 */
public interface StorageService {
	void save(DataObject data);
}
