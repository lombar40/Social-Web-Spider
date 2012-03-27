package edu.unlv.cs673.socialwebspider.spider;

import javax.swing.text.html.*;

/**
 * The following is used in the crawler4j example.
 */
public class HTMLParse extends HTMLEditorKit {

	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * returns the HTML Parser.
	 */
	public final HTMLEditorKit.Parser getParser() {
		return super.getParser();
	}
}