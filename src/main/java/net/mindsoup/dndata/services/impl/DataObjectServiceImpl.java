package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.exceptions.SecurityException;
import net.mindsoup.dndata.exceptions.UserInputException;
import net.mindsoup.dndata.helpers.JsonSchemaHelper;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.ObjectStatusWithUser;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.ObjectStatusDAO;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.repositories.ObjectRepository;
import net.mindsoup.dndata.repositories.ObjectStatusRepository;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.JsonValidatorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Valentijn on 3-8-2019
 */
@Service
public class DataObjectServiceImpl implements DataObjectService {

	private JsonValidatorService jsonValidatorService;
	private ObjectRepository objectRepository;
	private ObjectStatusRepository objectStatusRepository;

	@Autowired
	public DataObjectServiceImpl(JsonValidatorService jsonValidatorService,
								 ObjectRepository objectRepository,
								 ObjectStatusRepository objectStatusRepository) {
		this.jsonValidatorService = jsonValidatorService;
		this.objectRepository = objectRepository;
		this.objectStatusRepository = objectStatusRepository;
	}

	@Override
	public DataObject save(DataObject data, String comment) {
		improveDataObject(data);
		if(data.getId() == null) {
			return create(data, comment);
		} else {
			return update(data, comment);
		}
	}

	@Override
	public DataObject updateStatus(DataObject dataObject, String comment, ObjectStatus status) {
		ObjectStatusDAO objectStatus = createObjectStatusForObject(dataObject, comment, status);
		objectStatusRepository.save(objectStatus);
		return dataObject;
	}

	@Override
	public Iterable<DataObject> getAllForBook(Long bookId) {

		return objectRepository.findAllByBookIdAndStatusIn(bookId, Arrays.stream(ObjectStatus.values()).map(Enum::name).collect(Collectors.toList()));
	}

	@Override
	public Iterable<DataObject> getAllForBookAndStatuses(Long bookId, List<ObjectStatus> statuses) {
		return objectRepository.findAllByBookIdAndStatusIn(bookId, statuses.stream().map(Enum::name).collect(Collectors.toList()));
	}

	@Override
	public Iterable<ObjectStatusDAO> getAllStatusesForObject(DataObject dataObject) {
		return objectStatusRepository.findAllByObjectId(dataObject.getId());
	}

	@Override
	public List<ObjectStatusWithUser> getAllStatusesWithNamesForObject(DataObject dataObject) {
		List<ObjectStatusWithUser> returnList = new LinkedList<>();
		Iterable<Object[]> objects = objectStatusRepository.findAllStatusesWithNameByObjectId(dataObject.getId());
		for(Object[] objectArray : objects) {
			ObjectStatusWithUser status = new ObjectStatusWithUser();
			status.setObjectStatus((ObjectStatusDAO)objectArray[0]);
			status.setUser((User)objectArray[1]);
			returnList.add(status);
		}

		return returnList;
	}

	@Override
	public DataObject getForId(Long id) {
		return objectRepository.findLastById(id).orElse(null);
	}

	private DataObject create(DataObject dataObject, String comment) {
		if(dataObject.getBookId() == null || StringUtils.isBlank(dataObject.getName()) || dataObject.getType() == null) {
			throw new UserInputException("Book ID, name or type is not specified");
		}

		dataObject.setRevision(1);
		dataObject = objectRepository.save(dataObject);

		ObjectStatusDAO objectStatus = createObjectStatusForObject(dataObject, comment, ObjectStatus.CREATED);
		objectStatusRepository.save(objectStatus);

		return dataObject;
	}

	private DataObject update(DataObject dataObject, String comment) {
		if(dataObject.getBookId() == null || StringUtils.isAnyBlank(dataObject.getName(), comment) || dataObject.getType() == null) {
			throw new UserInputException("Book ID, name, comment or type is not specified");
		}

		/*if(!isValid(dataObject)) {
			throw new JsonValidationException("JSON Validation failed");
		}*/

		dataObject.setRevision(dataObject.getRevision() + 1);
		dataObject = insertUpdate(dataObject);

		ObjectStatusDAO objectStatus = createObjectStatusForObject(dataObject, comment, ObjectStatus.EDITING);
		objectStatusRepository.save(objectStatus);

		return dataObject;
	}

	private ObjectStatusDAO createObjectStatusForObject(DataObject dataObject, String comment, ObjectStatus status) {
		if(!SecurityHelper.isAuthenticated()) {
			throw new SecurityException("User not logged in");
		}

		User me = SecurityHelper.getAuthenticatedUser();

		ObjectStatusDAO objectStatus = new ObjectStatusDAO();
		objectStatus.setObjectId(dataObject.getId());
		objectStatus.setObjectRevision(dataObject.getRevision());
		objectStatus.setEditorId(me.getId());
		objectStatus.setComment(comment);
		objectStatus.setStatus(status);

		return objectStatus;
	}

	private boolean isValid(DataObject dataObject) {
		return jsonValidatorService.validate(dataObject.getObjectJson(), JsonSchemaHelper.getSchemaForData(dataObject)).isValid();
	}

	private DataObject improveDataObject(DataObject dataObject) {
		dataObject.setName(WordUtils.capitalizeFully(dataObject.getName()));
		return dataObject;
	}


	public DataObject insertUpdate(DataObject dataObject) {
		objectRepository.saveInsert(dataObject.getId(), dataObject.getRevision(), dataObject.getObjectJson(), dataObject.getSchemaVersion(), dataObject.getName(), dataObject.getType().toString(), dataObject.getBookId());

		return dataObject;
	}
}
