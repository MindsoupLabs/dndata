package net.mindsoup.dndata.controllers.restcontrollers;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectType;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by Valentijn on 21-3-2020
 */
@RestController
@RequestMapping(value = "/schema", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class SchemaController extends ErrorController {

	@RequestMapping("/{game}/{type}/{version}")
	public String schema(@PathVariable(value = "game") Game game, @PathVariable(value = "type") ObjectType type, @PathVariable(value = "version") int version) throws IOException {
		String filename = String.format("/json-schemas/%s/%s/v%s.json", game.name().toLowerCase(), type.name().toLowerCase(), version);
		InputStream inputStream = getClass().getResourceAsStream(filename);
		if(inputStream == null) {
			throw new FileNotFoundException(String.format("Schema file %s not found", filename));
		}
		return IOUtils.toString(inputStream, Charset.defaultCharset());
	}
}
