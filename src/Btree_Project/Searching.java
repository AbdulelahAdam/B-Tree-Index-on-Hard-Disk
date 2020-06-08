package Btree_Project;

import java.util.ArrayList;
import java.util.Stack;

public class Searching extends TreeOperations {
	private Stack<Integer> stack = new Stack<>();

	public Searching() {
	}

	public int find_in_node(ArrayList<Integer> _node, int value) {
		int size = _node.size();
		for (int i = 1; i < size; i += 2) {
			if (_node.get(i) == value) {
				System.out.println("\nValue: " + value + " exists in file with offset: " + _node.get(i + 1));
				return _node.get(i + 1);
			}
		}
		return -1;
	}

	public int SearchARecord(int value) throws Exception {
		int i = 1, index;
		node = GetNodeAt(i);
		for (int j = 1; j < node.size(); j++) {
			if (node.get(j) == value) {
				stack.push(i);
				break;
			}
		}
		while (true) {
			if (node.get(0) == 0) {
				index = find_in_node(node, value);
				if (index == -1) {
					System.out.println("\nValue does not exist in index file.");
					return -1;
				} else
					return index;
			} else {
				boolean temp = false;
				int size = node.size();
				for (int j = 1; j < size; j++) {
					if (node.get(j) >= value) {
						i = node.get(j + 1);
						temp = true;
						break;
					}
				}
				if (!temp) {
					return -1;
				}
			}
			node = GetNodeAt(i);
			stack.push(i);
		}
	}

	public Stack<Integer> Search_for_delete(int value) throws Exception {
		int index = SearchARecord(value);
		if (index == -1) {
			return null;
		} else {
			return stack;
		}
	}

}