package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.exceptions.JsonValidationException;
import net.mindsoup.dndata.exceptions.UserInputException;
import net.mindsoup.dndata.helpers.JsonSchemaHelper;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.repositories.ObjectRepository;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.JsonValidatorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Valentijn on 3-8-2019
 */
@Service
public class DataObjectServiceImpl implements DataObjectService {

	private JsonValidatorService jsonValidatorService;
	private ObjectRepository objectRepository;

	@Autowired
	public DataObjectServiceImpl(JsonValidatorService jsonValidatorService, ObjectRepository objectRepository) {
		this.jsonValidatorService = jsonValidatorService;
		this.objectRepository = objectRepository;
	}

	@Override
	public DataObject save(DataObject data) {
		if(data.getId() == null) {
			return create(data);
		} else {
			return update(data);
		}
	}

	@Override
	public Iterable<DataObject> getAllForBook(Long bookId) {
		return objectRepository.findAllByBookId(bookId);
	}

	private DataObject create(DataObject dataObject) {
		if(dataObject.getBookId() == null || StringUtils.isBlank(dataObject.getName()) || dataObject.getType() == null) {
			throw new UserInputException("Book ID, name or type is not specified");
		}

		return objectRepository.save(dataObject);
	}

	private DataObject update(DataObject dataObject) {
		if(dataObject.getBookId() == null || StringUtils.isBlank(dataObject.getName()) || dataObject.getType() == null) {
			throw new UserInputException("Book ID, name or type is not specified");
		}

		if(isValid(dataObject)) {
			dataObject.setRevision(dataObject.getRevision() + 1);
			return objectRepository.save(dataObject);
		} else {
			throw new JsonValidationException("JSON Validation failed");
		}
	}

	private boolean isValid(DataObject dataObject) {
		return jsonValidatorService.validate(dataObject.getObjectJson(), JsonSchemaHelper.getSchemaForData(dataObject)).isValid();
	}
}
