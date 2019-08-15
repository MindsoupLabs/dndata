package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.exceptions.JsonValidationException;
import net.mindsoup.dndata.exceptions.SecurityException;
import net.mindsoup.dndata.exceptions.UserInputException;
import net.mindsoup.dndata.helpers.PathHelper;
import net.mindsoup.dndata.helpers.SecurityHelper;
import net.mindsoup.dndata.models.DataObjectAndObjectStatus;
import net.mindsoup.dndata.models.ObjectStatusWithUser;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.DataObjectwithStatus;
import net.mindsoup.dndata.models.dao.ObjectStatusDAO;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.repositories.ObjectRepository;
import net.mindsoup.dndata.repositories.ObjectStatusRepository;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.JsonValidatorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Valentijn on 3-8-2019
 */
@Service
public class DataObjectServiceImpl implements DataObjectService {

	private static Log logger = LogFactory.getLog(DataObjectServiceImpl.class);

	private JsonValidatorService jsonValidatorService;
	private ObjectRepository objectRepository;
	private ObjectStatusRepository objectStatusRepository;
	@PersistenceContext
	private EntityManager entityManager;

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
		logger.info(String.format("Updated object '%s' with id %s to status %s", dataObject.getName(), dataObject.getId(), status.name()));
		return dataObject;
	}

	@Override
	public Iterable<DataObjectwithStatus> getAllForBook(Long bookId) {
		return getAllForBookAndStatuses(bookId, Arrays.asList(ObjectStatus.values()));
	}

	@Override
	public Iterable<DataObjectwithStatus> getAllForBookAndStatuses(Long bookId, List<ObjectStatus> statuses) {
		Iterable<Object[]> result = objectRepository.findAllByBookIdAndStatusInAsObjectArray(bookId, statuses.stream().map(Enum::name).collect(Collectors.toList()));

		List<DataObjectwithStatus> list = new LinkedList<>();

		result.forEach(a -> {
			DataObjectwithStatus resultObject = new DataObjectwithStatus();
			resultObject.setId( (Integer)a[0] );
			resultObject.setRevision((Integer)a[1]);
			resultObject.setObjectJson((String)a[2]);
			resultObject.setSchemaVersion((Integer)a[3]);
			resultObject.setName((String)a[4]);
			resultObject.setType(ObjectType.valueOf((String)a[5]));
			resultObject.setBookId( ((BigInteger)a[6]).longValueExact() );
			resultObject.setStatus(ObjectStatus.valueOf((String)a[7]));
			resultObject.setStatusId( ((BigInteger)a[8]).longValueExact() );

			list.add(resultObject);
		});

		return list;
	}

	@Override
	public Iterable<ObjectStatusDAO> getAllStatusesForObject(DataObject dataObject) {
		return objectStatusRepository.findAllByObjectId(dataObject.getId());
	}

	@Override
	public DataObject getForId(Integer id) {
		entityManager.clear();
		return objectRepository.findLastById(id).orElse(null);
	}

	@Override
	public DataObject getForIdAndRevision(Integer id, Integer revision) {
		entityManager.clear();
		return objectRepository.findByIdAndRevision(id, revision).orElse(null);
	}

	@Override
	public ObjectStatusDAO getStatusById(Long id) {
		return objectStatusRepository.findById(id).orElse(null);
	}

	@Override
	public List<DataObject> getUnpublishedObjectsForBook(Long bookId, Date updatedSince) {
		Iterable<Object[]> results = objectRepository.findObjectAndStatusByBookAndStatusTypeAndAfterDate(bookId, ObjectStatus.REVIEWED, updatedSince);
		List<DataObjectAndObjectStatus> interpretedResults = convertObjectArrayToObject(results);

		List<DataObject> dataObjects = interpretedResults.stream().map(DataObjectAndObjectStatus::getDataObject).collect(Collectors.toList());
		return filterOutDuplicates(dataObjects);
	}

	@Override
	public List<DataObject> getAllPublishableObjectsForType(ObjectType type) {
		List<DataObject> returnList = new LinkedList<>();
		objectRepository.findObjectByTypeAndStatusTypeIn(type, Arrays.asList(ObjectStatus.REVIEWED, ObjectStatus.PUBLISHED)).forEach(returnList::add);
		return filterOutDuplicates(returnList);
	}

	@Override
	public List<DataObject> getAllPublishableObjectsForBook(Long bookId) {
		List<DataObject> returnList = new LinkedList<>();
		objectRepository.findObjectsByBookAndStatusTypeIn(bookId, Arrays.asList(ObjectStatus.REVIEWED, ObjectStatus.PUBLISHED)).forEach(returnList::add);
		return filterOutDuplicates(returnList);
	}

	@Override
	public List<DataObject> getAllPublishableObjects() {
		List<DataObject> returnList = new LinkedList<>();
		objectRepository.findObjectsByStatusTypeIn(Arrays.asList(ObjectStatus.REVIEWED, ObjectStatus.PUBLISHED)).forEach(returnList::add);
		return filterOutDuplicates(returnList);
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

	private DataObject create(DataObject dataObject, String comment) {
		if(dataObject.getBookId() == null || StringUtils.isBlank(dataObject.getName()) || dataObject.getType() == null) {
			throw new UserInputException("Book ID, name or type is not specified");
		}

		dataObject.setRevision(1);
		dataObject = objectRepository.save(dataObject);
		entityManager.clear();
		dataObject = objectRepository.findById(dataObject.getInternalId()).orElse(null);

		ObjectStatusDAO objectStatus = createObjectStatusForObject(dataObject, comment, ObjectStatus.CREATED);
		objectStatusRepository.save(objectStatus);

		logger.info(String.format("Created %s object '%s' with id %s", dataObject.getType().name(), dataObject.getName(), dataObject.getId()));

		return dataObject;
	}

	private DataObject update(DataObject dataObject, String comment) {
		if(dataObject.getBookId() == null || StringUtils.isAnyBlank(dataObject.getName(), comment) || dataObject.getType() == null) {
			throw new UserInputException("Book ID, name, comment or type is not specified");
		}

		if(!isValid(dataObject)) {
			logger.warn(String.format("Object '%s' with id %s failed validation on update", dataObject.getName(), dataObject.getId()));
			throw new JsonValidationException("JSON Validation failed");
		}

		dataObject.setRevision(dataObject.getRevision() + 1);
		dataObject = objectRepository.save(dataObject);

		ObjectStatusDAO objectStatus = createObjectStatusForObject(dataObject, comment, ObjectStatus.EDITING);
		objectStatusRepository.save(objectStatus);

		logger.info(String.format("Updated object '%s' with id %s, current status: %s", dataObject.getName(), dataObject.getId(), objectStatus.getStatus().name()));

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
		return jsonValidatorService.validate(dataObject.getObjectJson(), PathHelper.getSchema(Game.PF2, dataObject.getType(), dataObject.getSchemaVersion())).isValid();
	}

	private DataObject improveDataObject(DataObject dataObject) {
		dataObject.setName(WordUtils.capitalizeFully(dataObject.getName()));
		return dataObject;
	}


	public DataObject insertUpdate(DataObject dataObject) {
		objectRepository.saveInsert(dataObject.getId(), dataObject.getRevision(), dataObject.getObjectJson(), dataObject.getSchemaVersion(), dataObject.getName(), dataObject.getType().toString(), dataObject.getBookId());

		return dataObject;
	}

	private List<DataObjectAndObjectStatus> convertObjectArrayToObject(Iterable<Object[]> result) {
		List<DataObjectAndObjectStatus> interpretedResults = new LinkedList<>();
		result.forEach(ar -> interpretedResults.add(new DataObjectAndObjectStatus((DataObject)ar[0], (ObjectStatusDAO)ar[1])));
		return interpretedResults;
	}

	private List<DataObject> filterOutDuplicates(List<DataObject> dataObjects) {
		Map<Integer, DataObject> objectsMap = new HashMap<>();

		dataObjects.forEach(d -> {
			if(!objectsMap.containsKey(d.getId()) || objectsMap.get(d.getId()).getRevision() < d.getRevision()) {
				objectsMap.put(d.getId(), d);
			}
		});

		return new LinkedList<>(objectsMap.values());
	}
}
