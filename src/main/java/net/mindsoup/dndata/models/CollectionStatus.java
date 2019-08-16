package net.mindsoup.dndata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.helpers.PathHelper;
import net.mindsoup.dndata.models.dao.PublishData;

import java.util.Date;

/**
 * Created by Valentijn on 16-8-2019
 */
public class CollectionStatus {
	private Game game;
	private String name;
	private int revision;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date publishedDate;
	private String archiveLocation;
	private String updateMessage;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getArchiveLocation() {
		return archiveLocation;
	}

	public void setArchiveLocation(String archiveLocation) {
		this.archiveLocation = archiveLocation;
	}

	public String getUpdateMessage() {
		return updateMessage;
	}

	public void setUpdateMessage(String updateMessage) {
		this.updateMessage = updateMessage;
	}

	public static CollectionStatus fromPublishData(PublishData publishData, String baseUrl) {
		CollectionStatus collectionStatus = new CollectionStatus();

		collectionStatus.setGame(publishData.getGame());
		collectionStatus.setName(publishData.getName());
		collectionStatus.setPublishedDate(publishData.getPublishedDate());
		collectionStatus.setRevision(publishData.getRevision());
		collectionStatus.setArchiveLocation(baseUrl + PathHelper.getZipFilePath(publishData.getGame(), publishData.getName(), publishData.getRevision()));
		collectionStatus.setUpdateMessage(publishData.getUpdateMessage());

		return collectionStatus;
	}
}
