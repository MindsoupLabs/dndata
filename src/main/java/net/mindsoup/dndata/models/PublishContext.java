package net.mindsoup.dndata.models;

import net.mindsoup.dndata.enums.Game;

/**
 * PublishContext
 */
public class PublishContext {
	private Game game;
	private String identifier;
	private int revision;

	public PublishContext(Game game, String identifier, int revision) {
		this.game = game;
		this.identifier = identifier;
		this.revision = revision;
	}

	public Game getGame() {
		return game;
	}

	public String getIdentifier() {
		return identifier;
	}

	public int getRevision() {
		return revision;
	}
}
