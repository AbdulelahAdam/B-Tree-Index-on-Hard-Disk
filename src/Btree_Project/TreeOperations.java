package Btree_Project;

import java.io.RandomAccessFile;

import java.util.ArrayList;

public class TreeOperations {
	static int m_descendants;
	static int records;
	static int pointer = 1;
	static TreeNode n;
	static FileManagement file = new FileManagement();
	static RandomAccessFile rand;
	static ArrayList<Integer> node = new ArrayList<Integer>();
	static int length;

	public TreeOperations() {
	}

	/**
	 * constructor
	 * 
	 * @param _num
	 * @param _size
	 */
	public TreeOperations(int _num, int _size) {
		this.m_descendants = 1 + (_num * 2);
		this.records = _size;
		this.length = m_descendants * 4;
		n = new TreeNode(m_descendants, length);
	}

	public void CreateIndexFileFile(String fileName, int _size, int m_desc) throws Exception {
		file.setFilename(fileName);
		this.m_descendants = 1 + (m_desc * 2);
		this.records = _size;
		this.length = m_descendants * 4;
		n = new TreeNode(m_descendants, length);
		intitialize();
	}

	public void intitialize() throws Exception {
		node = new ArrayList<>();
		rand = file.forwrite();
		int count = 1;
		while (count <= records) {
			for (int i = 0; i < m_descendants; i++) {
				if (i != 1 || count == records)
					node.add(-1);
				else {
					node.add(count);
				}
			}
			n.writeNode(node, rand);
			node = new ArrayList<>();
			count++;
		}
		rand.close();

	}

	public void DisplayIndexFileContent() throws Exception {
		rand = file.forread();
		int i = 0;
		while (i < records) {
			node = n.readNode(rand, i);
			System.out.println("record(" + (i) + "): " + node);
			i++;
		}
		rand.close();
	}

	public ArrayList<Integer> GetNodeAt(int record) throws Exception {
		node = new ArrayList<Integer>();
		int Seek = record * length;
		rand = file.forread();
		rand.seek(Seek);

		if (record == 1)
			record++;

		node = n.readNode(rand, record - 1);
		rand.close();
		return node;
	}

	public int GetFreenode() throws Exception {
		rand = file.forread();
		rand.seek(4);
		int x = rand.readInt();
		rand.close();
		return x;
	}

	public static boolean checkleaf(int index) throws Exception {
		rand = file.forread();
		rand.seek(index * length);
		int leaf = rand.readInt();
		rand.close();
		if (leaf == 0)
			return true;
		else if (leaf == 1)
			return false;
		else {
			rand = file.forwrite();
			rand.seek(index * length);
			rand.writeInt(0);
			rand.close();
			return true;
		}
	}

	public static ArrayList<Integer> Sortnode(ArrayList<Integer> _node) {
		int sz = _node.size();
		for (int i = 1; i < sz; i += 2) {
			int min = i;
			for (int j = i + 2; j < sz; j += 2) {
				if (_node.get(j) < _node.get(min) && (_node.get(min) != -1 && _node.get(j) != -1))
					min = j;
			}
			int swap1 = _node.get(i);
			int swap2 = _node.get(i + 1);

			_node.set(i, _node.get(min));
			_node.set(i + 1, _node.get(min + 1));
			_node.set(min, swap1);
			_node.set(min + 1, swap2);
		}

		return _node;
	}

	public static int GetFreeIndexInNode(ArrayList<Integer> _node) {
		int sz = _node.size();
		for (int i = 1; i < sz; i += 2) {
			if (_node.get(i) == -1 || _node.get(i + 1) == -1) {
				return i;
			}
		}
		return -1;
	}

	public static ArrayList<Integer> freeNode(ArrayList<Integer> _node) {
		_node = new ArrayList<>();
		for (int i = 0; i < m_descendants; i++) {
			_node.add(-1);
		}
		return _node;
	}

	public static int GetIndex_OF_MaxValue(ArrayList<Integer> _node) {
		int max = 1, size = _node.size();
		for (int i = 3; i < size; i += 2) {
			if (_node.get(i) > _node.get(max))
				max = i;
		}
		return max;
	}

	public int GetNum_Values_inNode(ArrayList<Integer> _node) {
		int count = 0;
		int i = 1, size = _node.size();
		for (; i < size; i += 2) {
			if (_node.get(i) != -1)
				count++;
		}
		return count;
	}

	public int GetIndexOf(ArrayList<Integer> _node, int value) {
		int size = _node.size();
		int i = 1;
		for (; i < size; i += 2) {
			if (_node.get(i) == value) {
				return i;
			}
		}

		return -1;
	}

}
