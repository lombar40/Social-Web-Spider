package edu.unlv.cs673.socialwebspider.spider.controller;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;

public interface CrawlerController {

	/**
	 * The following is used to start a new spidering.
	 * 
	 * Example Values: configFolder spider/config/UUID numberOfCrawlers 1
	 * storageFolder spider/images/UUID maxDepth 3 politenessDelay 200
	 * entryPoint http://www.funnypix.ca/main.php
	 * 
	 * @param userId
	 *            The user's unique id that identifies them in the database..
	 * @param userSpecificCategoryId
	 *            The id of the .
	 * @param configFolder
	 *            This is where the configuration settings are stored for a
	 *            crawl.
	 * @param numberOfCrawlers
	 *            The number of crawlers (threads).
	 * @param storageFolder
	 *            Where the output of the spidering goes, e.g. images files.
	 * @param maxDepth
	 *            Max link depth from the entryPoint.
	 * @param politenessDelay
	 *            200 <-- Minimum for spidering laws.
	 * @param entryPoint
	 *            URL of where to start.
	 * @param minBinarySize
	 *            Minimum size of binary in bytes.
	 * @throws Exception
	 *             Possible exception that can be thrown.
	 */
	void startSpider(String username, int userSpecificCategoryId, String configFolder, int numberOfCrawlers, String storageFolder, 
			int maxDepth, int politenessDelay, String entryPoint, int minBinarySize) throws Exception;

	/**
	 * Takes all binaries in a folder and stores them to the database based on userId and user specific category.
	 * 
	 * @param userId					User's Id.
	 * @param blobBinaryFolderPath		Path of where the spidering results are.
	 * @param userSpecificCategoryId	Category to assign for these spiderings.
	 */
	void storeSpiderResults(String username, String storageFolder, int userSpecificCategoryId);

	/**
	 * Used for database connection clean up and thing else that needs to happen.
	 * 
	 * @param configFolder		Temp configure folder used by crawler4j.
	 * @param storageFolder		Temp storage folder used by crawler4j.
	 */
	void finishSpider(String configFolder, String storageFolder);
	
	/**
	 * Setups the CrawlConfig, needed and used by crawler4j.
	 * 
	 * @param configFolder
	 *            A unique folder where crawler4j can store its configuration
	 *            files.
	 * @param maxDepth
	 *            Max depth of spidering.
	 * @param politenessDelay
	 *            Politeness delay when spidering.
	 * @return
	 */
	public CrawlConfig setupCrawlConfig(final String configFolder, final int maxDepth, final int politenessDelay);
	
	/**
	 * Setups the CrawlController, needed and used by crawler4j.
	 * 
	 * @param entryPoint
	 *            The entry point for our spidering.
	 * @param config
	 *            The CrawlConfig, used by crawler4j.
	 * @return Returns the CrawlController that crawler4j can use.
	 * @throws Exception
	 *             Possible exceptions that can be thrown.
	 */
	public CrawlController setupCrawlController(final String entryPoint, CrawlConfig config) throws Exception;
	
	/**
	 * The following is used to start a new crawl.
	 * 
	 * Example Values: configFolder spider/config/UUID numberOfCrawlers 1
	 * storageFolder spider/images/UUID maxDepth 3 politenessDelay 200
	 * entryPoint http://www.funnypix.ca/main.php
	 * 
	 * @param configFolder
	 *            This is where the configuration settings are stored for a
	 *            crawl.
	 * @param numberOfCrawlers
	 *            The number of crawlers (threads).
	 * @param storageFolder
	 *            Where the output of the spidering goes, e.g. images files.
	 * @param maxDepth
	 *            Max link depth from the entryPoint.
	 * @param politenessDelay
	 *            200 <-- Minimum for spidering laws.
	 * @param entryPoint
	 *            URL of where to start.
	 * @param minBinarySize
	 *            Minimum size of binary in bytes.
	 * @throws Exception
	 *             Possible exception that can be thrown.
	 */
	public void startNewCrawler(final String configFolder, final int numberOfCrawlers, final String storageFolder, final int maxDepth, final int politenessDelay, final String entryPoint,
			final int minBinarySize) throws Exception;

}
