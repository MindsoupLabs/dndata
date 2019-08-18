import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.helpers.PathHelper;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

/**
 * Created by Valentijn on 19-8-2019
 */
public class ZipFileTests {

	@Test
	public void legal_txt_exists() throws URISyntaxException, IOException {
		File file = new ClassPathResource(PathHelper.getLegalPath(Game.PF2)).getFile();
		assertTrue(file.exists());
	}
}
