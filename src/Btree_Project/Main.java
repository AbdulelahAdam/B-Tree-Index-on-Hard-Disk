package Btree_Project;

import java.util.*;

public class Main {

	static Scanner userInput = new Scanner(System.in);

	public static void main(String[] args) throws Exception {

		System.out.println("B-Tree Index Operations on Hard Disk");

		int desc, recNum;
		System.out.print("Descendants: ");
		desc = userInput.nextInt();
		System.out.print("Number of records: ");
		recNum = userInput.nextInt();

		TreeOperations file = new TreeOperations(desc, recNum);

		file.CreateIndexFileFile("IndexFile.txt", recNum, desc);

		Insertion I = new Insertion();
		Deletion D = new Deletion();

		System.out.println("\nBelow is the contents of the established Index File.");
		I.DisplayIndexFileContent();
		System.out.println("\n");

		// All these records' offsets are assumptions since no offsets were given in the
		// research's document test case
		System.out.println("\nBelow is the contents of the Index File after insertion.");
		I.InsertNewRecordAtIndex(5, 12);
		I.InsertNewRecordAtIndex(3, 24);
		I.InsertNewRecordAtIndex(21, 48);
		I.InsertNewRecordAtIndex(9, 60);
		I.InsertNewRecordAtIndex(1, 72);
		I.InsertNewRecordAtIndex(13, 84);
		I.InsertNewRecordAtIndex(2, 96);
		I.InsertNewRecordAtIndex(7, 108);
		I.InsertNewRecordAtIndex(10, 120);

		I.DisplayIndexFileContent();
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("\n");

		// Deletion

		D.DeleteRecordFromIndex(10);
		System.out.println("\nIndex File content after Deletion.");
		I.DisplayIndexFileContent();
		System.out.println("\n");

		D.DeleteRecordFromIndex(10);
		System.out.println("\nIndex File content after Deletion.");
		I.DisplayIndexFileContent();
		System.out.println("\n");

		D.DeleteRecordFromIndex(21);
		System.out.println("\nIndex File content after Deletion.");
		I.DisplayIndexFileContent();
		System.out.println("\n");

	}

}
