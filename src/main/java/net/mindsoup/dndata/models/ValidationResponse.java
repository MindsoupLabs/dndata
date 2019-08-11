package net.mindsoup.dndata.models;

import net.mindsoup.dndata.exceptions.JsonValidationException;
import org.everit.json.schema.ValidationException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Valentijn on 11-8-2019
 */
public class ValidationResponse {

	private boolean valid;
	private List<String> validationProblems = new LinkedList<>();

	public ValidationResponse(ValidationResult result) {
		this.valid = result.isValid();
		if(result.getValidationException() instanceof ValidationException) {
			((ValidationException)result.getValidationException()).getCausingExceptions().forEach(e -> validationProblems.add(e.getMessage()));
		} else {
			validationProblems.add(result.getValidationException().getMessage());
		}
	}

	public boolean isValid() {
		return valid;
	}

	public List<String> getValidationProblems() {
		return validationProblems;
	}
}
