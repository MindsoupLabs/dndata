package net.mindsoup.dndata.helpers;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.enums.ObjectStatus;

/**
 * Created by Valentijn on 14-8-2019
 */
public class HtmlHelper {

	public static String getColorClass(ObjectStatus status) {
		switch (status) {
			case CREATED:
				return Constants.Status.Color.CREATED;
			case EDITING:
				return Constants.Status.Color.EDITING;
			case AWAITING_REVIEW:
				return Constants.Status.Color.AWAITING_REVIEW;
			case REVIEWED:
				return Constants.Status.Color.REVIEWED;
			case PUBLISHED:
				return Constants.Status.Color.PUBLISHED;
			case DELETED:
				return Constants.Status.Color.DELETED;
		}

		return "";
	}

	public static String getIcon(ObjectStatus status) {
		switch (status) {
			case CREATED:
				return Constants.Status.Icon.CREATED;
			case EDITING:
				return Constants.Status.Icon.EDITING;
			case AWAITING_REVIEW:
				return Constants.Status.Icon.AWAITING_REVIEW;
			case REVIEWED:
				return Constants.Status.Icon.REVIEWED;
			case PUBLISHED:
				return Constants.Status.Icon.PUBLISHED;
			case DELETED:
				return Constants.Status.Icon.DELETED;
		}

		return "";
	}
}
