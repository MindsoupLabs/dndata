package net.mindsoup.dndata.helpers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectType;

/**
 * Created by Valentijn on 3-8-2019
 */
public class PathHelper {

	private final static String JSON_SCHEMA_ROOT = "json-schemas";
	private final static String LEGAL_ROOT = "legal";

	public static String getSchema(Game game, ObjectType type, int version) {
		return String.format("/%s/%s/%s/v%s.json", JSON_SCHEMA_ROOT, game.name().toLowerCase(), type.name().toLowerCase(), version);
	}

	public static String getFormSchema(Game game, ObjectType type, int version) {
		String uiSchema = String.format("/%s/%s/%s/ui/v%s.json", JSON_SCHEMA_ROOT, game.name().toLowerCase(), type.name().toLowerCase(), version);

		// load special ui-friendly schema if it exists (to get around allOf etc restrictions)
		if(PathHelper.class.getResource(uiSchema) != null) {
			return uiSchema;
		}

		// otherwise just return regular schema
		return getSchema(game, type, version);
	}

	public static String getUISchema(Game game, ObjectType type, int version) {
		return String.format("/%s/%s/%s/ui/v%s.uiSchema.json", JSON_SCHEMA_ROOT, game.name().toLowerCase(), type.name().toLowerCase(), version);
	}

	public static String getZipFilePath(Game game, String identifier, int version) {
		return String.format("%s/%s.%s.zip", game.name().toLowerCase(), identifier, version);
	}

	public static String getJsonFilePath(String identifier, int version) {
		return String.format("%s.%s.json", identifier, version);
	}

	public static String getLegalPath(Game game) {
		return String.format("/%s/%s/%s", LEGAL_ROOT, game.name().toLowerCase(), Constants.Files.LEGAL_TEXT);
	}
}
