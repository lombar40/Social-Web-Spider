package edu.unlv.cs673.socialwebspider.spider.controller;

import edu.unlv.cs.socialwebspider.domain.Category;
import edu.unlv.cs.socialwebspider.domain.Spider;
import edu.unlv.cs673.socialwebspider.uuid.UUIDFactoryImpl;

public class CrawlerThread implements Runnable {

	private String myUUID;
	private String configFolder;
	private String storageFolder;
	private String startURL;
	private String username;
	private Category category;
	private Long id;
	
	/**
	 * Prep work for spider thread.
	 * 
	 * @param configBase	Relative path for config folder. e.g. "spider/configs/"
	 * @param storageBase	Relative path for storage folder. e.g. "spider/images/"
	 * @param startURL		Where to start the spider. e.g. "http://www.funnypix.ca/main.php"
	 */
	public CrawlerThread(String configBase, String storageBase, String startURL, String username, Category category, Long id) {
		System.out.println("CrawlerThread constructor start");
		UUIDFactoryImpl UUIDFactory = new UUIDFactoryImpl();
		myUUID = UUIDFactory.generateUUID();
		configFolder = configBase + myUUID;
		storageFolder = storageBase + myUUID;
		this.startURL = startURL;
		this.username = username;
		this.category = category;
		this.id = id;
		System.out.println("CrawlerThread constructor end");
	}
	
	@Override
	public void run() {
		System.out.println("CrawlerThread run() start");
		Spider currentSpider = Spider.findSpider(this.id);
		
		CrawlerImagesOnlyControllerImpl crawlController = new CrawlerImagesOnlyControllerImpl();
		try {
			crawlController.startSpider(username, category.getId().intValue(), configFolder, 1, storageFolder, 1, 20, startURL, BinarySizes.TWENTY_KB);
			currentSpider.setStatus("Completed");
		} catch (Exception e) {
			currentSpider.setStatus("Error");
			e.printStackTrace();
		}
		currentSpider.merge();
		System.out.println("CrawlerThread run() end");
	}

}