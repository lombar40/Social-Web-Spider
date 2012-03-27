package edu.unlv.cs673.database;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class BlobHandlerTest {

	@Test
	public void insertRowTest() {
		try{
			BlobHandler b = new BlobHandler();
			b.insertRow(1, "jamesbtest1.jpg", "spider/images/jamesbtest1.jpg", "Picture of James, when he was skinny.");
	
			// Insert works so good to go.
			assert(true);
		}catch(Exception e){
			e.printStackTrace();
			fail("Error occurred when trying to upload a binary to the db.");
		}
	}

	@Test
	public void getRowTest() {
		try{
			BlobHandler b = new BlobHandler();
			String currentDir = new File("").getAbsolutePath();
			System.out.println("currentDir: " + currentDir);
			
			String outputDir = "/spider/images/output";
			// Make sure out directory exists.
			File f = new File(outputDir);
			if(!f.exists()){
				f.mkdir();
			}
			
			b.getRow(2, currentDir + outputDir);
			assert(true);
		}catch(Exception e){
			e.printStackTrace();
			fail("Error occurred when trying to get binary from db.");
		}
	}
	
	@Test
	public void insertBlobsFromFolderTest() {
		try{
			BlobHandler b = new BlobHandler();
			String currentDir = new File("").getAbsolutePath();
			System.out.println("currentDir: " + currentDir);
			
			String targetDir = currentDir + "/spider/images/bulkBinaryTest";
			// Make sure out directory exists.
			File f = new File(targetDir);
			if(!f.exists()){
				f.mkdir();
			}
			
			// Do insert of all binaries of particular folder. Gave unique category id of -1134, so easy to see test.
			b.insertBlobsFromFolder(2, targetDir, -1134);
			
			assert(true);
		}catch(Exception e){
			e.printStackTrace();
			fail("Error occurred when trying to get binary from db.");
		}
	}

}
