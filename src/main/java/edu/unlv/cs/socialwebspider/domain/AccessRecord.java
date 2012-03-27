package edu.unlv.cs.socialwebspider.domain;

import java.awt.List;

public class AccessRecord {
	int totalRecords = 0;
	List files = new List(); // List of file Ids, note that each Id is kept as a String
	
	// This adds a file to the list of files that can be accessed 
	// by the user
	public void addFileId(String fileId)
	{
		files.add(fileId);
		totalRecords++;
	}
}
