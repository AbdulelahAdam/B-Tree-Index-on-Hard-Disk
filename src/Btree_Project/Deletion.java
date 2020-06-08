package Btree_Project;

import java.util.ArrayList;
import java.util.Stack;

public class Deletion extends TreeOperations {
	private Stack<Integer> stack = new Stack<Integer>();
	private Insertion insertObj = new Insertion();
	private Searching searchObj = new Searching();

	private ArrayList<Integer> Node = new ArrayList<Integer>();

	public Deletion() {
	}

	public ArrayList<Integer> RearrangeNode(ArrayList<Integer> _node) {
		ArrayList<Integer> list = new ArrayList<>();
		int size = _node.size();
		for (int i = 0; i < size; i++) {
			if (_node.get(i) != -1) {
				list.add(_node.get(i));
			}
		}
		size = size - list.size();
		for (int i = 0; i < size; i++)
			list.add(-1);
		return list;
	}

	public int searchNode(ArrayList<Integer> list, int value) {
		for (int i = 2; i < list.size(); i += 2) {
			if (list.get(i) == value) {
				return i - 1;
			}
		}
		return -1;
	}

	public void update(int pointer, int maxvalue, int nodepointer) throws Exception {
		Node = GetNodeAt(nodepointer);
		int index = searchNode(Node, pointer);
		if (index == -1)
			return;
		Node.set(index, maxvalue);
		insertObj.insertnode(Node, nodepointer);
		if (!stack.empty()) {
			update(nodepointer, maxvalue, stack.pop());
		}
	}

	public void DeleteRecordFromIndex(int value) throws Exception {
		stack = searchObj.Search_for_delete(value);

		if (stack == null)
			return;

		int index = stack.pop();
		Node = GetNodeAt(index);
		int numValues = GetNum_Values_inNode(Node);
		int maxIndex = GetIndex_OF_MaxValue(Node);
		int maxvalue = Node.get(maxIndex);

		if (numValues > 2) {
			if (value < maxvalue) {
				int valueIndex = GetIndexOf(Node, value);
				Node.set(valueIndex, -1);
				Node.set(valueIndex + 1, -1);
				Node = RearrangeNode(Node);
				Node = Sortnode(Node);
				insertObj.insertnode(Node, index);
			} else {
				Node.set(maxIndex, -1);
				Node.set(maxIndex + 1, -1);
				Node = Sortnode(Node);
				insertObj.insertnode(Node, index);
				maxIndex = GetIndex_OF_MaxValue(Node);
				maxvalue = Node.get(maxIndex);
				update(index, maxvalue, stack.pop());
			}
		} else {
			int valueIndex = GetIndexOf(Node, value);
			Node.set(valueIndex, -1);
			Node.set(valueIndex + 1, -1);
			Node = RearrangeNode(Node);
			if (index != 1)
				Merge_or_Reditribute(index);
			else {
				System.out.println(index + "    node=" + Node);
				insertObj.insertnode(Node, index);

				numValues = GetNum_Values_inNode(Node);
				if (numValues == 0) {
					System.out.println("\n");
					intitialize();
				}

				return;
			}
		}
		checkRoot();
	}

	public void checkIfEmptyRoot() throws Exception {
		Node = freeNode(Node);
		int freeindex = GetFreenode();
		Node.set(1, freeindex);

		insertObj.insertnode(Node, 1);
		insertObj.insert_at(1, -1, 0);

	}

	public void Merge_or_Reditribute(int index) throws Exception {

		int index1 = stack.pop();
		ArrayList<Integer> list = new ArrayList<>();
		list = GetNodeAt(index1);

		int leftNodeIndex = searchNode(list, index) - 1;
		ArrayList<Integer> leftNode = new ArrayList<>();

		boolean goleft = false, goright = false, weHaveLeft = false, weHaveRight = false;
		int leftNum = 0;
		if (leftNodeIndex > 1) {
			weHaveLeft = true;

			leftNodeIndex = list.get(leftNodeIndex);
			leftNode = GetNodeAt(leftNodeIndex);
			leftNum = GetNum_Values_inNode(leftNode);
			if (leftNum > 2)
				goleft = true;
		}

		int rigthNodeIndex = 0;
		ArrayList<Integer> rightNode = new ArrayList<>();
		int rightNum = 0;

		if (list.size() > searchNode(list, index) + 3)
			if (list.get(searchNode(list, index) + 3) != -1) {
				weHaveRight = true;
				rigthNodeIndex = searchNode(list, index) + 3;
				rigthNodeIndex = list.get(rigthNodeIndex);
				rightNode = GetNodeAt(rigthNodeIndex);
				rightNum = GetNum_Values_inNode(rightNode);
				if (rightNum > 2)
					goright = true;
			}
		if (goleft == true) {
			int maxleft = leftNode.get(GetIndex_OF_MaxValue(leftNode));
			int maxpointer = leftNode.get(GetIndex_OF_MaxValue(leftNode) + 1);
			int empty = GetFreeIndexInNode(Node);

			Node.set(empty, maxleft);
			Node.set(empty + 1, maxpointer);
			Node = Sortnode(Node);
			insertObj.insertnode(Node, index);

			int max = Node.get(GetIndex_OF_MaxValue(Node));
			maxleft = GetIndex_OF_MaxValue(leftNode);

			leftNode.set(maxleft, -1);
			leftNode.set(maxleft + 1, -1);
			insertObj.insertnode(leftNode, leftNodeIndex);

			maxleft = leftNode.get(GetIndex_OF_MaxValue(leftNode));
			for (int i = 2; i < list.size(); i++) {
				if (list.get(i) == leftNodeIndex) {
					list.set(i - 1, maxleft);
					break;
				}
			}

			insertObj.insertnode(list, index1);
			update(index, max, index1);

		} else if (goright == true) {

			int minright = rightNode.get(1);
			int minpointer = rightNode.get(2);

			rightNode.set(1, -1);
			rightNode.set(2, -1);
			rightNode = RearrangeNode(rightNode);

			insertObj.insertnode(rightNode, rigthNodeIndex);

			int empty = GetFreeIndexInNode(Node);

			Node.set(empty, minright);
			Node.set(empty + 1, minpointer);
			Node = Sortnode(Node);
			insertObj.insertnode(Node, index);

			for (int i = 2; i < list.size(); i++) {
				if (list.get(i) == index) {
					list.set(i - 1, minright);
					break;
				}
			}
			insertObj.insertnode(list, index1);
		} else {

			int value = Node.get(1);
			int pointer = Node.get(2);
			Node.set(1, -1);
			Node.set(2, -1);
			if (weHaveRight == true) {

				int empty = GetFreeIndexInNode(rightNode);
				rightNode.set(empty, value);
				rightNode.set(empty + 1, pointer);
				rightNode = Sortnode(rightNode);
				insertObj.insertnode(rightNode, rigthNodeIndex);

				int freenode = GetFreenode();
				insertObj.insert_at(index, -1, 0);
				Node.set(1, freenode);
				Node.set(0, -1);
				insertObj.insertnode(Node, index);

				index = searchNode(list, index);

				list.set(index, -1);
				list.set(index + 1, -1);
				list = RearrangeNode(list);
				Node = list;

				if (index1 == 1) {
					insertObj.insertnode(Node, index1);
					return;
				}
				int num = GetNum_Values_inNode(Node);
				if (num < 2)
					Merge_or_Reditribute(index1);
				else
					insertObj.insertnode(Node, index1);

			} else if (weHaveLeft == true) {
				int empty = GetFreeIndexInNode(leftNode);
				leftNode.set(empty, value);
				leftNode.set(empty + 1, pointer);
				leftNode = Sortnode(leftNode);
				insertObj.insertnode(leftNode, leftNodeIndex);

				int freenode = GetFreenode();
				insertObj.insert_at(index, -1, 0);
				Node.set(1, freenode);
				Node.set(0, -1);
				insertObj.insertnode(Node, index);
				int x = searchNode(list, leftNodeIndex);
				list.set(x, value);
				x = searchNode(list, index);
				list.set(x, -1);
				list.set(x + 1, -1);
				list = RearrangeNode(list);
				Node = list;
				if (index1 == 1) {
					insertObj.insertnode(Node, index1);
					return;
				}
				int var = stack.pop();
				int num = GetNum_Values_inNode(Node);
				update(index1, value, var);
				stack.push(var);
				Node = list;
				if (num < 2) {
					Merge_or_Reditribute(index1);
				} else
					insertObj.insertnode(Node, index1);
			}
		}

	}

	public void checkRoot() throws Exception {

		node = GetNodeAt(1);
		int Numvalues = GetNum_Values_inNode(node);

		if (Numvalues < 2) {
			int pointer = node.get(2);
			Node = GetNodeAt(pointer);
			node = freeNode(node);
			int freenode = GetFreenode();
			insertObj.insert_at(pointer, -1, 0);
			node.set(1, freenode);
			insertObj.insertnode(node, pointer);
			insertObj.insertnode(Node, 1);
		}
	}

}
