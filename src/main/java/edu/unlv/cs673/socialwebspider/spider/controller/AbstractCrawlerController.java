package edu.unlv.cs673.socialwebspider.spider.controller;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.unlv.cs673.database.BlobHandler;
import edu.unlv.cs673.socialwebspider.util.files.FolderManipImpl;

public abstract class AbstractCrawlerController implements CrawlerController {
	/**
	 * Need access to a blob handler for storing blobs to the database.
	 */
	protected BlobHandler bh = null;

	/**
	 * Default constructor.
	 */
	public AbstractCrawlerController() {
		super();
	}

	@Override
	public void storeSpiderResults(String username, String blobBinaryFolderPath, int userSpecificCategoryId, String entryPoint) {
		bh.insertBlobsFromFolder(username, blobBinaryFolderPath, userSpecificCategoryId, entryPoint);
	}

	@Override
	public void finishSpider(String configFolder, String storageFolder) {
		System.out.println("Start db close");
		// Close db connection.
		bh.closeDbConn();
		System.out.println("End db close");

		System.out.println("Start delete folders");
		// Delete temp folders.
		FolderManipImpl fm = new FolderManipImpl();

		// TODO: Get delete configFolder working properly.
		/*
		 * The delete of the configFolder is not working properly, it throws a
		 * stack trace related to "Unable to delete file". We had the same issue
		 * with the storageFolder and after some googling I found that the issue
		 * was related to file streams not being closed properly. After some
		 * time and reworking the code, I was able to get the storageFolder to
		 * work properly. I spend a couple hours trying to get this to work for
		 * the config folder as well, however it looks like the file handle is
		 * being kept somewhere in the crawler4j code. I did extra per cautions
		 * such as moving the close() into finally blocks, and looking through
		 * the code many times to verify I didn't miss an open file stream.
		 * Anyway, the config folders are only 8k each, so this will work
		 * properly for our initial version of the project. Making a note and
		 * TODO about this issue, in case things go smooth and we have more time
		 * to look at this more.
		 * 
		 * http://stackoverflow.com/questions/2143217/unable-to-delete-a-file-after
		 * -reading-it
		 */
		// fm.deleteFolder(configFolder); // This config folder looks to be
		// created and held onto by crawler4j without proper close.
		fm.deleteFolder(storageFolder);
		System.out.println("End delete folders");
	}

	@Override
	public CrawlConfig setupCrawlConfig(final String configFolder, final int maxDepth, final int politenessDelay) {
		/**
		 * Set up the config so crawler4j to do our bidding...
		 */
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(configFolder);
		config.setMaxDepthOfCrawling(maxDepth);
		config.setPolitenessDelay(politenessDelay);
		config.setIncludeBinaryContentInCrawling(true);
		return config;
	}

	@Override
	public CrawlController setupCrawlController(final String entryPoint, CrawlConfig config) throws Exception {
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		controller.addSeed(entryPoint);
		return controller;
	}

}
