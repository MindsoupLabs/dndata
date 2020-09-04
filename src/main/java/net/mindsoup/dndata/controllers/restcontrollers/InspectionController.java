package net.mindsoup.dndata.controllers.restcontrollers;

import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.services.DataObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

/**
 * Created by Valentijn on 5-9-2020
 */
@RestController
@RequestMapping(value = "/inspect", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class InspectionController extends ErrorController {

	@Autowired
	DataObjectService dataObjectService;

	@RequestMapping("/object/{id}/{revision}")
	public String getObjectJson(@PathVariable(value = "id") int id, @PathVariable(value = "revision") int revision) throws FileNotFoundException {
		DataObject dataObject = dataObjectService.getForIdAndRevision(id, revision);

		if(dataObject == null) {
			throw new FileNotFoundException(String.format("No object found with ID %s and revision %s", id, revision));
		}

		return dataObject.getObjectJson();
	}
}
