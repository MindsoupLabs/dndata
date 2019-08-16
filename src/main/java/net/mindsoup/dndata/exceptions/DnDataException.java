package net.mindsoup.dndata.exceptions;

/**
 * Created by Valentijn on 3-8-2019
 */
public class DnDataException extends RuntimeException {

	public DnDataException(String message) {
		super(message);
	}

	public DnDataException(Exception e) {
		super(e);
	}
}
