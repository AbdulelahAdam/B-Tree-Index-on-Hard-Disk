package Btree_Project;

import java.util.ArrayList;
import java.util.Stack;

public class Insertion extends TreeOperations {
	private int freenode;
	private Stack<Integer> stack = new Stack<Integer>();
	private ArrayList<Integer> list = new ArrayList<>();


	public Insertion() {}
	
	public Insertion(int _num, int _size) {}

	private int inserted_node = -1;

	@SuppressWarnings("deprecation")
	public int InsertNewRecordAtIndex(int value, int _pointer) throws Exception {
		inserted_node = -1;
		freenode = new Integer(GetFreenode());
		if (freenode == 1) {
			inserted_node = 1;
			insert_at(value, _pointer, 1);
			node = GetNodeAt(1);
			int empty = GetFreeIndexInNode(node);
			if (empty == -1) {
				insert_at(freenode + 1, -1, 0);
			}
		} else {
			stack = new Stack<Integer>();
			stack.push(1);
			loop_insert(value, _pointer, 1);
		}

		return inserted_node;
	}

	public void insert_at(int value, int _pointer, int index) throws Exception {
		if (_pointer == -1) {
			node = GetNodeAt(index);
			if (value >= records)
				value = -1;
			node.set(1, value);
			rand = file.forwrite();
			rand.seek(0);
			n.writeNode(node, rand);
			rand.close();
		} else {
			node = GetNodeAt(index);
			if (freenode == 1)
				node.set(0, 0);
			int empty_index = GetFreeIndexInNode(node);
			node.set(empty_index, value);
			node.set(empty_index + 1, _pointer);
			node = Sortnode(node);
			rand = file.forwrite();
			rand.seek(length * index);
			n.writeNode(node, rand);
			rand.close();
		}
	}

	public void insertnode(ArrayList<Integer> _node, int index) throws Exception {
		_node = Sortnode(_node);
		rand = file.forwrite();
		rand.seek(length * index);
		n.writeNode(_node, rand);
		rand.close();
	}
	


	public void update(int max_list2, int list2_index, int max_list1, int list1_index) throws Exception {
		int index = stack.pop();
		node = GetNodeAt(index);
		int empty_index = GetFreeIndexInNode(node);

		int var = 0;
		for (int i = 2; i < node.size(); i += 2) {
			if (node.get(i) == list1_index) {
				var = i;
				break;
			}
		}
		node.set(var - 1, max_list1);
		if (empty_index != -1) {
			node.set(empty_index, max_list2);
			node.set(empty_index + 1, list2_index);
			node = Sortnode(node);
			insertnode(node, index);
		} else {
			ArrayList<Integer> list1 = new ArrayList<Integer>();
			ArrayList<Integer> list2 = new ArrayList<Integer>();

			node.add(max_list2);
			node.add(list2_index);

			node = Sortnode(node);

			list1 = freeNode(list1);
			list2 = freeNode(list2);

			int i = 1;
			int sz = node.size();

			int availsize = (sz - 1) / 2;

			if (availsize % 2 != 0)
				availsize++;

			for (; i <= availsize; i++) {
				list1.set(i, node.get(i));
			}
			int count = 1;

			i = availsize + 1;

			for (; i < sz; i++) {
				list2.set(count++, node.get(i));
			}
			list1.set(0, 1);
			list2.set(0, 1);
			if (index == 1) {
				freenode = GetFreenode();
				if (freenode == -1) {
					System.out.println("all nodes are consumed.");
					return;
				}
				node = freeNode(node);
				node.set(0, 1);
				node.set(1, list1.get(GetIndex_OF_MaxValue(list1)));
				node.set(2, freenode);
				node.set(3, list2.get(GetIndex_OF_MaxValue(list2)));
				node.set(4, freenode + 1);
				insertnode(node, index);
				insertnode(list1, freenode);
				insertnode(list2, freenode + 1);
				freenode += 2;
				insert_at(freenode, -1, 0);

			} else {
				if (freenode == -1) {
					System.out.println("all nodes are consumed.");
					return;
				}
				insertnode(list1, freenode);
				insertnode(list2, freenode + 1);
				list2_index = freenode++;
				max_list2 = list2.get(GetIndex_OF_MaxValue(list2));
				list1_index = index;
				max_list1 = list1.get(GetIndex_OF_MaxValue(list1));
				insert_at(freenode + 1, -1, 0);
				System.out.println("Recursion update");
				update(max_list2, list2_index, max_list1, list1_index);
			}
		}
	}
	public int searchNode(ArrayList<Integer> list, int pointer) {
		for (int i = 2; i < list.size(); i += 2) {
			if (list.get(i) == pointer) {
				return i - 1;
			}
		}
		return -1;
	}
	public void update(int pointer, int maxvalue, int nodepointer) throws Exception {
		list = GetNodeAt(nodepointer);
		int index = searchNode(list, pointer);
		if (index == -1)
			return;
		list.set(index, maxvalue);
		insertnode(list, nodepointer);
		if (!stack.empty()) {
			update(nodepointer, maxvalue, stack.pop());
		}
	}


	public void loop_insert(int value, int pointer, int index) throws Exception {
		node = GetNodeAt(index);
		int size = node.size();
		int isleaf = node.get(0);
		if (isleaf == 0) {
			int empty_index = GetFreeIndexInNode(node);
			if (empty_index != -1) {

				int max1 = node.get(GetIndex_OF_MaxValue(node));
				node.set(empty_index, value);
				node.set(empty_index + 1, pointer);
				inserted_node = index;
				node = Sortnode(node);
				insertnode(node, index);
				int max2 = node.get(GetIndex_OF_MaxValue(node));
				int x = stack.pop();
				int y = stack.pop();
				if (max2 > max1)
					update(x, max2, y);
			} else {
				ArrayList<Integer> list1 = new ArrayList<Integer>();
				ArrayList<Integer> list2 = new ArrayList<Integer>();
				node.add(value);
				node.add(pointer);
				node = Sortnode(node);

				list1 = freeNode(list1);
				list2 = freeNode(list2);

				int i = 1;
				int sz = node.size();

				int availsize = (sz - 1) / 2;

				if (availsize % 2 != 0)
					availsize++;

				for (; i <= availsize; i++) {
					list1.set(i, node.get(i));
				}
				int count = 1;

				i = availsize + 1;

				for (; i < sz; i++) {
					list2.set(count++, node.get(i));
				}
				list1.set(0, 0);
				list2.set(0, 0);

				if (index == 1) {
					freenode = GetFreenode();
					if (freenode == -1) {
						System.out.println("all nodes are consumed.");
						return;
					}
					node = freeNode(node);
					node.set(0, 1);
					node.set(1, list1.get(GetIndex_OF_MaxValue(list1)));
					node.set(2, freenode);
					node.set(3, list2.get(GetIndex_OF_MaxValue(list2)));
					node.set(4, freenode + 1);

					insertnode(node, index);
					insertnode(list1, freenode);
					insertnode(list2, freenode + 1);
					freenode += 2;
					insert_at(freenode, -1, 0);
				} else {
					if (freenode == -1) {
						System.out.println("all nodes are consumed.");
						return;
					}
					insertnode(list1, stack.pop());
					insertnode(list2, freenode);

					int list2_index = freenode, max_list2 = list2.get(GetIndex_OF_MaxValue(list2));

					int list1_index = index, max_list1 = list1.get(GetIndex_OF_MaxValue(list1));
					insert_at(freenode + 1, -1, 0);
					update(max_list2, list2_index, max_list1, list1_index);
				}
			}
		} else {
			size = node.size();
			int i;
			boolean temp = false;
			for (i = 1; i < size; i += 2) {
				if (node.get(i) >= value) {
					temp = true;
					break;
				}
			}
			if (temp) {
				stack.push(node.get(i + 1));
				loop_insert(value, pointer, node.get(i + 1));
			} else {
				int max = GetIndex_OF_MaxValue(node);
				max = node.get(max + 1);
				stack.push(max);
				loop_insert(value, pointer, max);
			}

		} 
	}

}
