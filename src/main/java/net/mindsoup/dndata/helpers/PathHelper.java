package net.mindsoup.dndata.helpers;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.enums.ObjectType;

/**
 * Created by Valentijn on 3-8-2019
 */
public class PathHelper {

	public static String getSchema(Game game, ObjectType type, int version) {
		return String.format("/json-schemas/%s/%s/v%s.json", game.name().toLowerCase(), type.name().toLowerCase(), version);
	}

	public static String getForm(Game game, ObjectType type, int version) {
		return String.format("edit_forms/%s/%s/v%s.html", game.name().toLowerCase(), type.name().toLowerCase(), version);
	}
}
