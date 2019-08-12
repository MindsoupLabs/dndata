package net.mindsoup.dndata.helpers;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectType;

import java.io.File;

/**
 * Created by Valentijn on 3-8-2019
 */
public class PathHelper {

	private final static String JSON_SCHEMA_LOCATION = "/json-schemas/";

	public static String getSchema(Game game, ObjectType type, int version) {
		return String.format(JSON_SCHEMA_LOCATION + "%s/%s/v%s.json", game.name().toLowerCase(), type.name().toLowerCase(), version);
	}

	public static String getFormSchema(Game game, ObjectType type, int version) {
		String uiSchema = String.format(JSON_SCHEMA_LOCATION + "%s/%s/ui/v%s.json", game.name().toLowerCase(), type.name().toLowerCase(), version);

		// load special ui-friendly schema if it exists (to get around allOf etc restrictions)
		if(new File(PathHelper.class.getResource(uiSchema).getFile()).exists()) {
			return uiSchema;
		}

		// otherwise just return regular schema
		return getSchema(game, type, version);
	}

	public static String getUISchema(Game game, ObjectType type, int version) {
		return String.format(JSON_SCHEMA_LOCATION + "%s/%s/ui/v%s.uiSchema.json", game.name().toLowerCase(), type.name().toLowerCase(), version);
	}
}
