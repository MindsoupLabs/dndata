package net.mindsoup.dndata.repositories;

import net.mindsoup.dndata.models.dao.DataObject;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Valentijn on 3-8-2019
 */
public interface ObjectRepository extends CrudRepository<DataObject, Long> {
}
