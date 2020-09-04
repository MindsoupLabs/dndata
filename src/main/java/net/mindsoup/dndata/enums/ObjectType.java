package net.mindsoup.dndata.enums;

/**
 * Created by Valentijn on 3-8-2019
 */
public enum ObjectType {
	CREATURE, SPELL, FEAT, ITEM, AFFLICTION, RITUAL, MAGIC_ITEM;

	public static ObjectType[] getActiveObjectTypes() {
		return new ObjectType[]{FEAT, SPELL};
	}
}
