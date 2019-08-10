import net.mindsoup.dndata.enums.ObjectType;
import net.mindsoup.dndata.exceptions.JsonValidationException;
import net.mindsoup.dndata.models.dao.DataObject;
import net.mindsoup.dndata.models.dao.User;
import net.mindsoup.dndata.repositories.ObjectRepository;
import net.mindsoup.dndata.repositories.ObjectStatusRepository;
import net.mindsoup.dndata.services.DataObjectService;
import net.mindsoup.dndata.services.JsonValidatorService;
import net.mindsoup.dndata.services.impl.DataObjectServiceImpl;
import net.mindsoup.dndata.services.impl.JsonValidatorServiceImpl;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;

/**
 * Created by Valentijn on 3-8-2019
 */
public class DataObjectServiceTests {

	private ObjectRepository objectRepository;
	private ObjectStatusRepository objectStatusRepository;
	private DataObjectService dataObjectService;

	@Before
	public void setup() throws IOException {
		JsonValidatorService jsonValidatorService = new JsonValidatorServiceImpl();
		objectRepository = Mockito.mock(ObjectRepository.class);
		doReturn(getValidData()).when(objectRepository).save(any());
		objectStatusRepository = Mockito.mock(ObjectStatusRepository.class);
		dataObjectService = new DataObjectServiceImpl(jsonValidatorService, objectRepository, objectStatusRepository);
		User me = new User();
		me.setId(1L);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(me, "", new LinkedList<>()));
	}

	@Test
	public void test_storage_service_save_succeed() throws IOException {
		dataObjectService.save(getValidData(), "comment");
		Mockito.verify(objectRepository, times(1)).save(any());
		Mockito.verify(objectStatusRepository, times(1)).save(any());
	}

	@Test(expected = JsonValidationException.class)
	public void test_storage_service_save_fail() throws IOException {
		try {
			dataObjectService.save(getInvalidData(), "comment");
		} catch (Exception e) {
			Mockito.verify(objectRepository, times(0)).save(any());
			throw e;
		}
	}

	private DataObject getValidData() throws IOException {
		DataObject dataObject = new DataObject();
		dataObject.setId(1L);
		dataObject.setBookId(1L);
		dataObject.setType(ObjectType.CREATURE);
		dataObject.setName("creature");
		dataObject.setObjectJson(loadJsonFromResource("/json-schemas/creature.v1.json"));
		return dataObject;
	}

	private DataObject getInvalidData() throws IOException {
		DataObject dataObject = getValidData();
		dataObject.setObjectJson(loadJsonFromResource("/json-schemas/creature.invalid.json"));
		return dataObject;
	}

	private String loadJsonFromResource(String resourcePath) throws IOException {
		return IOUtils.toString(getClass().getResourceAsStream(resourcePath), Charset.defaultCharset());
	}
}
