package com.jamesoravec.example.uuid;

import java.util.UUID;

/**
 * From: http://www.javapractices.com/topic/TopicAction.do?Id=56
 * 
 * Used to generate Universal Unique Identifiers.
 */
public class GenerateUUID {

	public static final void main(String... aArgs) {
		// generate random UUIDs
		UUID idOne = UUID.randomUUID();
		UUID idTwo = UUID.randomUUID();
		log("UUID One: " + idOne);
		log("UUID Two: " + idTwo);
	}

	private static void log(Object aObject) {
		System.out.println(String.valueOf(aObject));
	}
}