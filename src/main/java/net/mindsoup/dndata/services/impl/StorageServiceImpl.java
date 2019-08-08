package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.exceptions.JsonValidationException;
import net.mindsoup.dndata.helpers.JsonSchemaHelper;
import net.mindsoup.dndata.models.DataObject;
import net.mindsoup.dndata.repositories.ObjectRepository;
import net.mindsoup.dndata.services.JsonValidatorService;
import net.mindsoup.dndata.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Valentijn on 3-8-2019
 */
@Service
public class StorageServiceImpl implements StorageService {

	private JsonValidatorService jsonValidatorService;
	private ObjectRepository objectRepository;

	@Autowired
	public StorageServiceImpl(JsonValidatorService jsonValidatorService, ObjectRepository objectRepository) {
		this.jsonValidatorService = jsonValidatorService;
		this.objectRepository = objectRepository;
	}

	@Override
	public void save(DataObject data) {
		if(isValid(data)) {
			data.setObjectVersion(data.getObjectVersion() + 1);
			objectRepository.save(data);
		} else {
			throw new JsonValidationException("JSON Validation failed");
		}
	}

	private boolean isValid(DataObject dataObject) {
		return jsonValidatorService.validate(dataObject.getObjectJson(), JsonSchemaHelper.getSchemaForData(dataObject)).isValid();
	}
}
