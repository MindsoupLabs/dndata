import net.mindsoup.dndata.services.JsonValidatorService;
import net.mindsoup.dndata.services.impl.JsonValidatorServiceImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Valentijn on 3-8-2019
 */
public class SchemaValidatorTests {

	@Test
	public void test_creature_validation() throws IOException {
		JsonValidatorService jsonValidatorService = new JsonValidatorServiceImpl();
		assertTrue(jsonValidatorService.validate(loadJsonFromResource("/json-schemas/creature.v1.json"), "/json-schemas/pf2/creature/v1.json").isValid());
	}

	@Test
	public void test_creature_invalidation() throws IOException {
		JsonValidatorService jsonValidatorService = new JsonValidatorServiceImpl();
		assertFalse(jsonValidatorService.validate(loadJsonFromResource("/json-schemas/creature.invalid.json"), "/json-schemas/pf2/creature/v1.json").isValid());
	}

	private String loadJsonFromResource(String resourcePath) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(resourcePath), Charset.defaultCharset());
	}
}
