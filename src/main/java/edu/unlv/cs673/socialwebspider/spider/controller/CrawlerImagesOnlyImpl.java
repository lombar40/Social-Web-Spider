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

package edu.unlv.cs673.socialwebspider.spider.controller;


/**
 * The following file is based on the ImageCrawlController example provided by
 * the crawler4j website. It has been modified so that it works with our
 * project.
 * 
 * Updates/Modifications by: James Oravec (http://www.jamesoravec.com)
 */
public class CrawlerImagesOnlyImpl extends CrawlerImpl {
	
	private int minImageSize;
	
	// These are patterns that need to be changed for different filtering, used by crawler4j. NOT YET IMPLEMENTED
	//private static Pattern patternToFilterOut = Pattern.compile(Patterns.FILTER_OUT_NON_IMAGES);
	//private static Pattern patternToSave = Pattern.compile(Patterns.PATERN_IMAGES);
	
	/**
	 * Default Constructor.
	 */
	public CrawlerImagesOnlyImpl() {
		super();
	}
	
	/**
	 * Used to setup and configure the crawler.
	 * 
	 * @param minImageSize
	 * @param storageFolderName
	 */
	CrawlerImagesOnlyImpl(final int minImageSize) {
		this.minImageSize = minImageSize;
	}
	

}