package net.mindsoup.dndata.services.impl;

import com.github.slugify.Slugify;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;
import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.helpers.PathHelper;
import net.mindsoup.dndata.models.PublishContext;
import net.mindsoup.dndata.models.ValidationResult;
import net.mindsoup.dndata.models.dao.Book;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.PublishData;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.JsonValidatorService;
import net.mindsoup.dndata.services.PublishDataService;
import net.mindsoup.dndata.services.PublishingService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Path;
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

	@Autowired
	public PublishingServiceImpl(PublishDataService publishDataService, DataObjectService dataObjectService, JsonValidatorService jsonValidatorService) {
		this.publishDataService = publishDataService;
		this.dataObjectService = dataObjectService;
		this.jsonValidatorService = jsonValidatorService;
	}

	@Override
	public void publish(Book book) throws IOException {
		logger.info(String.format("Publishing changes in book %s: %s", book.getId(), book.getName()));
		List<DataObject> objectsInThisBook = getUnpublishedDataForBook(book);

		validateObjects(book, objectsInThisBook);

		int bookRevision = getLastPublishRevision(publishDataService.getMostRecentPublishDataForBook(book));

		Set<ObjectType> publishingTypes = new HashSet<>();
		objectsInThisBook.forEach(o -> publishingTypes.add(o.getType()));

		Slugify slugify = new Slugify();

		logger.info(String.format("Publishing book %s: %s", book.getId(), book.getName()));
		createFileAndUpload(createObjectMapFromList(dataObjectService.getAllPublishableObjectsForBook(book.getId())), new PublishContext(book.getGame(), slugify.slugify(book.getName()), bookRevision));

		for(ObjectType type : publishingTypes) {
			logger.info(String.format("Publishing collection %s", type.name()));
			int typeRevision = getLastPublishRevision(publishDataService.getMostRecentPublishDataForType(type));
			createFileAndUpload(createObjectMapFromList(dataObjectService.getAllPublishableObjectsForType(type)), new PublishContext(book.getGame(), slugify.slugify(type.name()), typeRevision));
		}

		logger.info("Publishing collection ALL");
		int allRevision = getLastPublishRevision(publishDataService.getMostRecentPublishDataForName(Constants.Collections.ALL));
		createFileAndUpload(createObjectMapFromList(dataObjectService.getAllPublishableObjects()), new PublishContext(book.getGame(), Constants.Collections.ALL, allRevision));

		logger.info("Publishing completed");
	}

	@Override
	public List<DataObject> getUnpublishedDataForBook(Book book) {
		Date updatedSince = getLastPublishDate(publishDataService.getMostRecentPublishDataForBook(book));
		return dataObjectService.getUnpublishedObjectsForBook(book.getId(), updatedSince);
	}

	private Date getLastPublishDate(PublishData publishData) {
		Date updatedSince = new Date(0);

		if(publishData != null) {
			updatedSince = publishData.getPublishedDate();
		}

		return updatedSince;
	}

	private int getLastPublishRevision(PublishData publishData) {
		int revision = 1;

		if(publishData != null) {
			revision = publishData.getRevision();
		}

		return revision;
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

	private void createFileAndUpload(Map<ObjectType, List<JSONObject>> data, PublishContext context) throws IOException {
		logger.info("Creating JSON file");
		File jsonFile = writeJsonToFile(data);
		logger.info("Creating file archive");
		File zipFile = zipJsonFile(jsonFile, context);
		logger.info("Uploading archive");
		uploadFile(zipFile, PathHelper.getZipFilePath(context.getGame(), context.getIdentifier(), context.getRevision()));
		logger.info("Updating publishing data");
		updatePublishingData(context);
	}

	private File writeJsonToFile(Map<ObjectType, List<JSONObject>> data) throws IOException {
		File jsonFile = File.createTempFile(Constants.Files.TEMP_PREFIX, Constants.Files.JSON_SUFFIX);
		String jsonData = new Gson().toJson(data);
		FileWriter writer = new FileWriter(jsonFile);
		writer.write(jsonData);
		writer.close();

		return jsonFile;
	}

	private File zipJsonFile(File jsonFile, PublishContext context) throws IOException  {
		File zipFile = File.createTempFile(Constants.Files.TEMP_PREFIX, Constants.Files.ZIP_SUFFIX);
		FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
		ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
		FileInputStream fileInputStream = new FileInputStream(jsonFile);
		ZipEntry zipEntry = new ZipEntry(PathHelper.getJsonFilePath(context.getIdentifier(), context.getRevision()));
		zipOutputStream.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while((length = fileInputStream.read(bytes)) >= 0) {
			zipOutputStream.write(bytes, 0, length);
		}
		zipOutputStream.close();
		fileInputStream.close();
		fileOutputStream.close();

		return zipFile;
	}

	private void uploadFile(File file, String path) {

	}

	private void updatePublishingData(PublishContext context) {

	}
}
