package edu.unlv.cs673.socialwebspider.util.files;

public interface FolderManip {

	/**
	 * Creates the folder
	 * 
	 * @param folder
	 */
	void createFolder(String relativePath, String folderName);

	/**
	 * Deletes the folder based on the relative path given.
	 * 
	 * @param relativePath
	 */
	void deleteFolder(String relativePath);

	/**
	 * Create a folder with a universal unique name. Returns the name of the
	 * folder.
	 * 
	 * @param relativePath
	 *            The relative path of the UUID folder.
	 * @return The UUID folder name.
	 */
	String createUniqueFolder(String relativePath);

}
