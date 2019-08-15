package net.mindsoup.dndata.models;

/**
 * Created by Valentijn on 3-8-2019
 */
public class ValidationResult {
	private boolean valid;
	private RuntimeException validationException;

	public ValidationResult(boolean isValid) {
		this.valid = isValid;
	}

	public ValidationResult(boolean isValid, RuntimeException exception) {
		this.valid = isValid;
		this.validationException = exception;
	}

	public boolean isValid() {
		return valid;
	}

	public RuntimeException getValidationException() {
		return validationException;
	}
}
