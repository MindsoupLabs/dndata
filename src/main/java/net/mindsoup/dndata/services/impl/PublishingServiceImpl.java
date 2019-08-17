package net.mindsoup.dndata.services.impl;

import com.github.slugify.Slugify;
import com.google.gson.Gson;
import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectStatus;
import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.helpers.PathHelper;
import net.mindsoup.dndata.models.PublishContext;
import net.mindsoup.dndata.models.ValidationResult;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.PublishData;
import net.mindsoup.dndata.services.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Valentijn on 16-8-2019
 */
@Service
public class PublishingServiceImpl implements PublishingService {

	private static Log logger = LogFactory.getLog(PublishingServiceImpl.class);

	private PublishDataService publishDataService;
	private DataObjectService dataObjectService;
	private JsonValidatorService jsonValidatorService;
	private UploadService uploadService;
	private Slugify slugify;

	@Autowired
	public PublishingServiceImpl(PublishDataService publishDataService,
								 DataObjectService dataObjectService,
								 JsonValidatorService jsonValidatorService,
								 UploadService uploadService) {
		this.publishDataService = publishDataService;
		this.dataObjectService = dataObjectService;
		this.jsonValidatorService = jsonValidatorService;
		this.uploadService = uploadService;
		this.slugify = new Slugify();
	}

	@Override
	public void publish(Book book, String comment) throws IOException, URISyntaxException {
		logger.info(String.format("Publishing changes in book %s: %s", book.getId(), book.getName()));
		List<DataObject> objectsInThisBook = getUnpublishedDataForBook(book);

		validateObjects(book, objectsInThisBook);

		PublishData publishData = getPublishData(book, comment);

		Set<ObjectType> publishingTypes = new HashSet<>();
		objectsInThisBook.forEach(o -> publishingTypes.add(o.getType()));

		logger.info(String.format("Publishing book %s: %s", book.getId(), book.getName()));
		createFileAndUpload(createObjectMapFromList(dataObjectService.getAllPublishableObjectsForBook(book.getId())), new PublishContext(book.getGame(), publishData));

		comment = book.getName() + ": " + comment;
		for(ObjectType type : publishingTypes) {
			logger.info(String.format("Publishing collection %s", type.name()));
			publishData = getPublishData(type.name(), book.getGame(), comment);
			createFileAndUpload(createObjectMapFromList(dataObjectService.getAllPublishableObjectsForType(type)), new PublishContext(book.getGame(), publishData));
		}

		logger.info("Publishing collection ALL");
		publishData = getPublishData(Constants.Collections.COLLECTION_ALL, book.getGame(), comment);
		createFileAndUpload(createObjectMapFromList(dataObjectService.getAllPublishableObjects()), new PublishContext(book.getGame(), publishData));

		updatePublishedStatusForObjects(objectsInThisBook);
		logger.info("Publishing completed");
	}

	@Override
	public List<DataObject> getUnpublishedDataForBook(Book book) {
		Date updatedSince = getLastPublishDate(publishDataService.getMostRecentPublishDataForBook(book));
		return dataObjectService.getUnpublishedObjectsForBook(book.getId(), updatedSince);
	}

	private PublishData getPublishData(String name, Game game, String comment) {
		int revision = getRevisionFromPublishData(publishDataService.getMostRecentPublishDataForName(game, name));

		return getPublishData(game, revision, slugify.slugify(name), comment);
	}

	private PublishData getPublishData(Book book, String comment) {
		int revision = getRevisionFromPublishData( publishDataService.getMostRecentPublishDataForBook(book));
		PublishData publishData = getPublishData(book.getGame(), revision, slugify.slugify(book.getName()), comment);
		publishData.setBookId(book.getId());
		return publishData;
	}

	private PublishData getPublishData(Game game, int revision, String name, String comment) {
		PublishData publishData = new PublishData();
		publishData.setGame(game);
		publishData.setName(name);
		publishData.setUpdateMessage(comment);
		publishData.setRevision(revision + 1);
		publishData.setPublishedDate(new Date());

		return publishData;
	}

	private int getRevisionFromPublishData(PublishData publishData) {
		if(publishData != null) {
			return publishData.getRevision();
		}

		return 0;
	}

	private Date getLastPublishDate(PublishData publishData) {
		if(publishData != null) {
			return publishData.getPublishedDate();
		}

		return new Date(0);
	}

	private void validateObjects(Book book, List<DataObject> dataObjects) {
		for(DataObject dataObject : dataObjects) {
			ValidationResult validationResult =	jsonValidatorService.validate(dataObject.getObjectJson(), PathHelper.getSchema(book.getGame(), dataObject.getType(), dataObject.getSchemaVersion()));
			if(!validationResult.isValid()) {
				throw validationResult.getValidationException();
			}
		}
	}

	private Map<ObjectType, List<JSONObject>> createObjectMapFromList(List<DataObject> dataObjects) {
		Map<ObjectType,  List<JSONObject>> map = new HashMap<>();
		dataObjects.forEach(d -> {
			if(!map.containsKey(d.getType())) {
				map.put(d.getType(), new LinkedList<>());
			}
			map.get(d.getType()).add(new JSONObject(d.getObjectJson()));
		});

		return map;
	}

	private void createFileAndUpload(Map<ObjectType, List<JSONObject>> data, PublishContext context) throws IOException, URISyntaxException {
		logger.info("Creating JSON file");
		File jsonFile = writeJsonToFile(data);
		logger.info("Creating file archive");
		File zipFile = zipJsonFile(jsonFile, context);
		logger.info("Uploading archive");
		uploadFile(zipFile, PathHelper.getZipFilePath(context.getGame(), context.getIdentifier(), context.getRevision()));
		logger.info("Updating publishing data");
		updatePublishingData(context);
		jsonFile.delete();
		zipFile.delete();
	}

	private void updatePublishedStatusForObjects(List<DataObject> objectsInThisBook) {
		logger.info("Updating status for published objects");
		objectsInThisBook.forEach(this::publish);
	}

	private File writeJsonToFile(Map<ObjectType, List<JSONObject>> data) throws IOException {
		File jsonFile = File.createTempFile(Constants.Files.TEMP_PREFIX, Constants.Files.JSON_SUFFIX);
		String jsonData = new Gson().toJson(data);
		FileWriter writer = new FileWriter(jsonFile);
		writer.write(jsonData);
		writer.close();

		return jsonFile;
	}

	private File zipJsonFile(File jsonFile, PublishContext context) throws IOException, URISyntaxException {
		File zipFile = File.createTempFile(Constants.Files.TEMP_PREFIX, Constants.Files.ZIP_SUFFIX);
		FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

		addFileToZip(zipOutputStream, PathHelper.getJsonFilePath(context.getIdentifier(), context.getRevision()), jsonFile);
		addFileToZip(zipOutputStream, Constants.Files.LEGAL_TEXT, new File(context.getClass().getResource(PathHelper.getLegalPAth(context.getGame())).toURI()));

		zipOutputStream.close();
		fileOutputStream.close();

		return zipFile;
	}

	private void addFileToZip(ZipOutputStream zipOutputStream, String nameInZipfile, File file) throws IOException {
		if(!file.exists()) {
			logger.warn("Attempting to add non existing file " + file.getAbsolutePath() + " to zip archive. Aborting.");
			return;
		}

		logger.info("Attempting to add file " + file.getAbsolutePath() + " to zip archive.");

		ZipEntry zipEntry = new ZipEntry(nameInZipfile);
		zipOutputStream.putNextEntry(zipEntry);

		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] bytes = new byte[1024];
		int length;
		while((length = fileInputStream.read(bytes)) >= 0) {
			zipOutputStream.write(bytes, 0, length);
		}
		fileInputStream.close();
		logger.info("File added");
	}

	private void uploadFile(File file, String path) {
		uploadService.upload(file, path);
	}

	private void updatePublishingData(PublishContext context) {
		publishDataService.save(context.getPublishData());
	}

	private void publish(DataObject o) {
		dataObjectService.updateStatus(o, Constants.Comments.AUTO_COMMENT_PREFIX + Constants.Comments.PUBLISHED, ObjectStatus.PUBLISHED);
	}
}
