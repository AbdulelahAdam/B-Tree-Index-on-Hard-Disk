package Btree_Project;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class TreeNode {

	static ArrayList<Integer> list = new ArrayList<Integer>();
	static int len;
	static int size;

	public TreeNode() {
	}

	public TreeNode(int size, int len) {
		this.len = len;
		this.size = size;
	}

	public void writeNode(ArrayList<Integer> arr, RandomAccessFile raf) throws IOException {
		int sz = arr.size();
		int i = 0;
		while (i < sz) {
			raf.writeInt(arr.get(i));
			i++;
		}
	}

	public ArrayList<Integer> readNode(RandomAccessFile raf, int rnum) throws Exception {
		list = new ArrayList<>();
		int i = 0;
		while (i < size) {
			list.add(raf.readInt());
			i++;
		}
		return list;
	}

}