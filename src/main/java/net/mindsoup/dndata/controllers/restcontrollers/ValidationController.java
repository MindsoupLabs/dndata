package net.mindsoup.dndata.controllers.restcontrollers;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.helpers.PathHelper;
import net.mindsoup.dndata.models.ValidationResponse;
import net.mindsoup.dndata.services.JsonValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Valentijn on 11-8-2019
 */
@RestController
@RequestMapping(value = "/validation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ValidationController {

	private JsonValidatorService jsonValidatorService;

	@Autowired
	public ValidationController(JsonValidatorService jsonValidatorService) {
		this.jsonValidatorService = jsonValidatorService;
	}

	@RequestMapping("/{game}/{type}/{version}")
	public ValidationResponse validate(
			@PathVariable(value = "game") Game game,
			@PathVariable(value = "type") ObjectType type,
			@PathVariable(value = "version") int version ,
			@RequestBody String json) {
		return new ValidationResponse(jsonValidatorService.validate(json, PathHelper.getSchema(game, type, version)));
	}
}
