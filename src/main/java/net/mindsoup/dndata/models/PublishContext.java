package net.mindsoup.dndata.models;

import net.mindsoup.dndata.enums.Game;
import net.mindsoup.dndata.models.dao.PublishData;

/**
 * PublishContext
 */
public class PublishContext {
	private Game game;
	private PublishData publishData;

	public PublishContext(Game game, PublishData publishData) {
		this.game = game;
		this.publishData = publishData;
	}

	public Game getGame() {
		return game;
	}

	public String getIdentifier() {
		return publishData.getName();
	}

	public int getRevision() {
		return publishData.getRevision();
	}

	public PublishData getPublishData() {
		return publishData;
	}
}
