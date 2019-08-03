import net.mindsoup.dndata.exceptions.JsonValidationException;
import net.mindsoup.dndata.models.DataObject;
import net.mindsoup.dndata.repositories.ObjectRepository;
import net.mindsoup.dndata.services.JsonValidatorService;
import net.mindsoup.dndata.services.StorageService;
import net.mindsoup.dndata.services.impl.JsonValidatorServiceImpl;
import net.mindsoup.dndata.services.impl.StorageServiceImpl;
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
public class StorageServiceTests {

	private ObjectRepository objectRepository;
	private StorageService storageService;

	@Before
	public void setup() {
		JsonValidatorService jsonValidatorService = new JsonValidatorServiceImpl();
		objectRepository = Mockito.mock(ObjectRepository.class);
		storageService = new StorageServiceImpl(jsonValidatorService, objectRepository);
	}

	@Test
	public void test_storage_service_save_succeed() throws IOException {
		storageService.save(getValidData());
		Mockito.verify(objectRepository, times(1)).save(any());
	}

	@Test(expected = JsonValidationException.class)
	public void test_storage_service_save_fail() throws IOException {
		try {
			storageService.save(getInvalidData());
		} catch (Exception e) {
			Mockito.verify(objectRepository, times(0)).save(any());
			throw e;
		}
	}

	private DataObject getValidData() throws IOException {
		DataObject dataObject = new DataObject();
		dataObject.setObjectJson(loadJsonFromResource("/json-schemas/creature.v1.json"));
		return dataObject;
	}

	private DataObject getInvalidData() throws IOException {
		DataObject dataObject = new DataObject();
		dataObject.setObjectJson(loadJsonFromResource("/json-schemas/creature.invalid.json"));
		return dataObject;
	}

	private String loadJsonFromResource(String resourcePath) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(resourcePath), Charset.defaultCharset());
	}
}
