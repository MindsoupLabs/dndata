package net.mindsoup.dndata.controllers.restcontrollers;

import net.mindsoup.dndata.domain.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Valentijn on 3-8-2019
 */
public class ErrorController {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleException(Exception e) {
		ErrorResponse response = new ErrorResponse();
		response.setError(e.getClass().toString());
		response.setMessage(e.getMessage());
		return response;
	}
}
