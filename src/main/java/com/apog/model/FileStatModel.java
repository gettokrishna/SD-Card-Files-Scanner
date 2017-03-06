package com.apog.model;

import java.util.Comparator;

/**
 * This Class serves as a Model class to store the data attributes with respect to one file
 * @author Krishna Chaitanya Pavuluri
 */

public class FileStatModel {

	private String fileName;
	private String extn;
	private long lastModified;
	private long size;
	private String displaySize;
	
	public static final Comparator<FileStatModel> FileSizeComparator = new Comparator<FileStatModel>(){
		@Override
		public int compare(FileStatModel lhs, FileStatModel rhs) {
			long size1 = lhs.getSize();
			long size2 = rhs.getSize();
			return (size2<size1? -1 : (size2 ==size1? 0 : 1));
		}
	};
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getExtn() {
		return extn;
	}
	public void setExtn(String extn) {
		this.extn = extn;
	}
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getDisplaySize() {
		return displaySize;
	}
	public void setDisplaySize(String displaySize) {
		this.displaySize = displaySize;
	}

}
