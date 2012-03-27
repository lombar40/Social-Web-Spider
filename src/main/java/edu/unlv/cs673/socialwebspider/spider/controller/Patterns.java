package edu.unlv.cs673.socialwebspider.spider.controller;

/**
 * This class is used to supply a various patterns that can be used for save or filtering out files during the crawler4j spidering process.
 */
public class Patterns {
	/**
	 * Default Constructor.
	 */
	public Patterns() {
		super();
	}

	/* List of Patterns and Filters we can use */
	public static final String FILTER_OUT_NON_IMAGES = ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf|rm|smil|wmv|swf|wma|zip|rar|gz))$";
	public static final String FILTER_OUT_NON_GENERAL = ".*(\\.(css|js|ram|rm|smil|swf))$";
	public static final String PATERN_IMAGES = ".*(\\.(bmp|gif|jpe?g|png|tiff?))$";
	public static final String PATTERN_GENERAL = ".*(\\.(mid|mp2|mp3|mp4|wav|avi|mov|mpeg|m4v|pdf|bmp|wmv|wma|zip|rar|gz|gif|jpe?g|png|tiff?))$";
	
}
