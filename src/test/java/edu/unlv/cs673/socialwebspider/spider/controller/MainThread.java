package edu.unlv.cs673.socialwebspider.spider.controller;

public class MainThread {

	public static void main(String[] args) {
		System.out.println("main thread start");
		new Thread(new CrawlerThread("spider/configs/", "spider/images/", "http://www.funnypix.ca/main.php")).start();
		System.out.println("main thread returned");
		System.out.println("main thread end");
	}
}
