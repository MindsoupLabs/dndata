package net.mindsoup.dndata.services;

import net.mindsoup.dndata.models.ValidationResult;

/**
 * Created by Valentijn on 3-8-2019
 */
public interface JsonValidatorService {
	ValidationResult validate(String json, String schemaLocation);
}
