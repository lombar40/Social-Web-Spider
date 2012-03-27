/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.unlv.cs673.socialwebspider.spider;

import java.security.MessageDigest;

/**
 * The following is code that came in the example with Crawler4j.
 * Documentation/Updates from James Oravec (http://www.jamesoravec.com)
 * 
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class Cryptography {

	/**
	 * Hex Characters.
	 */
	private static final char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * Gets the MD5 hash.
	 * 
	 * @param str
	 *            Input string.
	 * @return
	 */
	public static String MD5(final String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return hexStringFromBytes(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Returns a hex string from a byte array.
	 * 
	 * @param byteArray
	 *            The byte array.
	 * @return
	 */
	private static String hexStringFromBytes(final byte[] byteArray) {
		String hex = "";
		int msb;
		int lsb;
		int i;

		// MSB maps to idx 0
		for (i = 0; i < byteArray.length; i++) {
			msb = ((int) byteArray[i] & 0x000000FF) / 16;
			lsb = ((int) byteArray[i] & 0x000000FF) % 16;
			hex = hex + hexChars[msb] + hexChars[lsb];
		}
		return (hex);
	}
}