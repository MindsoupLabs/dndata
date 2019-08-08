package net.mindsoup.dndata.controllers.restcontrollers;

import net.mindsoup.dndata.domain.ErrorResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Valentijn on 3-8-2019
 */
public class ErrorController {

	private static Log logger = LogFactory.getLog(ErrorController.class);

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(Exception e) {
		logger.error(String.format("Error %s bubbled up: %s", e.getClass().toString(), e.getMessage()));
		ErrorResponse response = new ErrorResponse();
		response.setError(e.getClass().toString());
		response.setMessage(e.getMessage());
		return response;
	}
}
