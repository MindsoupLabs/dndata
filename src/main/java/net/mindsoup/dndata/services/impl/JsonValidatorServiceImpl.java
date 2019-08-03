package net.mindsoup.dndata.services.impl;

import net.mindsoup.dndata.Application;
import net.mindsoup.dndata.models.ValidationResult;
import net.mindsoup.dndata.services.JsonValidatorService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

/**
 * Created by Valentijn on 3-8-2019
 */
@Service
public class JsonValidatorServiceImpl implements JsonValidatorService {

	private static Log logger = LogFactory.getLog(JsonValidatorServiceImpl.class);

	@Override
	public ValidationResult validate(String json, String schemaLocation) {
		try (InputStream inputStream = getClass().getResourceAsStream(schemaLocation)) {
			JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
			Schema schema = SchemaLoader.load(rawSchema);
			schema.validate(new JSONObject(json));
		} catch (IOException e) {
			logger.error(String.format("Error loading schema %s: %s", schemaLocation, e.getMessage()));
			return new ValidationResult(false, e);
		} catch (ValidationException e) {
			logger.warn(String.format("Json object %s did not validate against schema %s: %s", json, schemaLocation, e.getCausingExceptions().stream().map(ValidationException::getMessage).collect(Collectors.joining(", "))));
			return new ValidationResult(false, e);
		}

		return new ValidationResult(true);
	}
}
