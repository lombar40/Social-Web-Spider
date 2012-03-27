package edu.unlv.cs673.socialwebspider.uuid;

import java.util.UUID;

/**
 * Used to generate Universal Unique Identifiers.
 */
public class UUIDFactoryImpl implements UUIDFactory {

	/**
	 * Default constructor.
	 */
	public UUIDFactoryImpl() {
		// Do nothing.
	}

	/**
	 * Method that will generate a UUID in string format.
	 * 
	 * @return UUID as a String.
	 */
	public final String generateUUID() {
		return String.valueOf(UUID.randomUUID());
	}
}
