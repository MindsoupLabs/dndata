import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.exceptions.JsonValidationException;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.repositories.ObjectRepository;
import net.mindsoup.dndata.services.JsonValidatorService;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.impl.JsonValidatorServiceImpl;
import net.mindsoup.dndata.services.impl.DataObjectServiceImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

/**
 * Created by Valentijn on 3-8-2019
 */
public class DataObjectServiceTests {

	private ObjectRepository objectRepository;
	private DataObjectService dataObjectService;

	@Before
	public void setup() {
		JsonValidatorService jsonValidatorService = new JsonValidatorServiceImpl();
		objectRepository = Mockito.mock(ObjectRepository.class);
		dataObjectService = new DataObjectServiceImpl(jsonValidatorService, objectRepository);
	}

	@Test
	public void test_storage_service_save_succeed() throws IOException {
		dataObjectService.save(getValidData());
		Mockito.verify(objectRepository, times(1)).save(any());
	}

	@Test(expected = JsonValidationException.class)
	public void test_storage_service_save_fail() throws IOException {
		try {
			dataObjectService.save(getInvalidData());
		} catch (Exception e) {
			Mockito.verify(objectRepository, times(0)).save(any());
			throw e;
		}
	}

	private DataObject getValidData() throws IOException {
		DataObject dataObject = new DataObject();
		dataObject.setBookId(1L);
		dataObject.setType(ObjectType.CREATURE);
		dataObject.setName("creature");
		dataObject.setObjectJson(loadJsonFromResource("/json-schemas/creature.v1.json"));
		return dataObject;
	}

	private DataObject getInvalidData() throws IOException {
		DataObject dataObject = new DataObject();
		dataObject.setId(1L);
		dataObject.setBookId(1L);
		dataObject.setType(ObjectType.CREATURE);
		dataObject.setName("creature");
		dataObject.setObjectJson(loadJsonFromResource("/json-schemas/creature.invalid.json"));
		return dataObject;
	}

	private String loadJsonFromResource(String resourcePath) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(resourcePath), Charset.defaultCharset());
	}
}
