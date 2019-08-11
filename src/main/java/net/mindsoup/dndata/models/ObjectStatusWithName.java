package net.mindsoup.dndata.models;

import net.mindsoup.dndata.Constants;
import net.mindsoup.dndata.models.dao.ObjectStatusDAO;

import javax.persistence.Entity;

/**
 * Created by Valentijn on 11-8-2019
 */
@Entity
public class ObjectStatusWithName extends ObjectStatusDAO {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getColorClass() {
		switch (getStatus()) {
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
	
	public String getIcon() {
		switch (getStatus()) {
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
