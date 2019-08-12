package net.mindsoup.dndata.helpers;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectType;

import java.io.File;

/**
 * Created by Valentijn on 3-8-2019
 */
public class PathHelper {

	public static String getSchema(Game game, ObjectType type, int version) {
		return String.format("/json-schemas/%s/%s/v%s.json", game.name().toLowerCase(), type.name().toLowerCase(), version);
	}

	public static String getFormSchema(Game game, ObjectType type, int version) {
		String uiSchema = String.format("/json-schemas/%s/%s/ui/v%s.json", game.name().toLowerCase(), type.name().toLowerCase(), version);

		if(new File(PathHelper.class.getResource(uiSchema).getFile()).exists()) {
			return uiSchema;
		}

		return getSchema(game, type, version);
	}
}
