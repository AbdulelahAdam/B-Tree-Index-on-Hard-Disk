package Btree_Project;

import java.io.RandomAccessFile;

public class FileManagement {
	static RandomAccessFile raf;
	static RandomAccessFile write;
	static String filename = "file.txt";

	public RandomAccessFile forread() throws Exception {
		raf = new RandomAccessFile(filename, "r");
		raf.seek(0);
		return raf;
	}

	public static String getFilename() {
		return filename;
	}

	public static void setFilename(String filename) {
		FileManagement.filename = filename;
	}

	public RandomAccessFile forwrite() throws Exception {
		raf = new RandomAccessFile(filename, "rw");
		raf.seek(0);
		return raf;
	}

}
