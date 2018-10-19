
//Aviv Lazar
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HW1_AvivLazar {
	public static Scanner s = new Scanner(System.in);
	public static final String NAMES_FILE = "names.bin";
	public static final String WORKERS_FILE = "workers.bin";
	public static final String[] BOSS = { null, "Boss 1", "Boss 2", "Boss 3", "Boss 4" };
	public static final String SE = "Software Engineering";
	public static final String ME = "Mechanical Engineering";
	public static final String IME = "Industrial & Mechanical Engineering";
	public static final String ELE = "Electrical Engineering";

	public static final int MAXIMUM_CHARS_IN_WORKER_NAME = 20; // line 75, printf

	public static void main(String[] args) {

		// Adding the characters of the program
		System.out.println("Please Press 1 for dep as class Department, any other key for dep as String :");
		String str = s.nextLine();
		try {
			// Creating array according to user choice
			ArrayList<Worker<?>> arrayOfWorkers = createNewWorkersList(str);
			// Print all workers according to user choice
			printArray(arrayOfWorkers);
			// Save
			save(arrayOfWorkers);

			int numOfWorkers = arrayOfWorkers.size();
			// Worker 1
			System.out.print("\nPlease enter the first serial number: ");
			int wIndex1 = s.nextInt();
			String wName1 = searchWorkerNameInNamesFileByIndexAndPrintIt(wIndex1, numOfWorkers);

			// Worker 2
			System.out.print("\nPlease enter the second serial number: ");
			int wIndex2 = s.nextInt();
			String wName2 = searchWorkerNameInNamesFileByIndexAndPrintIt(wIndex2, numOfWorkers);

			// Compare Salaries
			System.out.println("\n\nFile content:");
			printArray();
			compareSalaries(wName1, wName2);
			System.out.println("\nFile content after comparing salaries:");
			printArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.getStackTrace();
		}
	}

	private static void printArray() throws FileNotFoundException, IOException {
		try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(WORKERS_FILE)))) {
			boolean isItDep = (in.readByte() == 1);
			int salary;
			String name, depName, headName;
			while (in.available() > 0) {
				name = in.readUTF();
				depName = in.readUTF();
				if (isItDep)
					headName = in.readUTF();
				else
					headName = "";
				salary = in.readInt();
				System.out.printf("%-20s   %-35s     %s     %-6d \n", name, depName, headName, salary);
			}
		}
	}

	private static void compareSalaries(String wName1, String wName2)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		try (RandomAccessFile r = new RandomAccessFile(WORKERS_FILE, "rw")) {
			int pos1 = -1, pos2 = -1, pos;
			Worker<?> worker1 = null, worker2 = null;
			boolean isItDep = (r.readByte() == 1);
			/*
			 * two conditions: 1. the File pointer didn't get to the end of file 2. all
			 * (two) workers havn't found yet
			 */
			while ((pos = (int) r.getFilePointer()) < r.length() && (pos1 == -1 || pos2 == -1)) {
				String name = r.readUTF();
				if (name.equalsIgnoreCase(wName1)) {
					pos1 = pos;
					worker1 = new Worker<>(name, isItDep ? new Department(r.readUTF(), r.readUTF()) : r.readUTF(),
							r.readInt());
				} else if (name.equalsIgnoreCase(wName2)) {
					pos2 = pos;
					worker2 = new Worker<>(name, isItDep ? new Department(r.readUTF(), r.readUTF()) : r.readUTF(),
							r.readInt());
				} else {
					r.readUTF(); // Dep name
					if (isItDep)
						r.readUTF(); // Dep head
					r.readInt();
				}
			}

			if (worker1.getSalary() > worker2.getSalary()) {
				wName1 = wName1.toUpperCase();
				r.seek(pos1);
				r.writeUTF(wName1);
			} else if (worker1.getSalary() < worker2.getSalary()) {
				wName2 = wName2.toUpperCase();
				r.seek(pos2);
				r.writeUTF(wName2);
			}
		}
	}

	private static String searchWorkerNameInNamesFileByIndexAndPrintIt(int wIndex, int numOfWorkers)
			throws FileNotFoundException, IOException {
		if (wIndex <= 0 || wIndex > numOfWorkers) {
			throw new IllegalArgumentException("There isn't such worker");
		}
		try (RandomAccessFile r = new RandomAccessFile(NAMES_FILE, "r")) {
			int pos = (wIndex - 1) * MAXIMUM_CHARS_IN_WORKER_NAME * 2; // seek is in unicode
			r.seek(pos);
			String wName = FixedLengthStringIO.readFixedLengthString(MAXIMUM_CHARS_IN_WORKER_NAME, r);
			System.out.printf("Name%d  =  %s\n", wIndex, wName);
			return wName.trim();
		}

	}

	private static void printArray(ArrayList<Worker<?>> arrayOfWorkers) {
		int i = 1, salary;
		String wName, depName, headName;
		boolean isItDep = isItDep(arrayOfWorkers.get(0));
		for (Worker<?> worker : arrayOfWorkers) {
			wName = worker.getName();
			depName = isItDep ? ((Department) worker.getDep()).getDepName() : (String) worker.getDep();
			headName = isItDep ? ((Department) worker.getDep()).getDepHead() : "";
			salary = worker.getSalary();
			System.out.printf("%d: %-20s   %-35s     %s     %-6d \n", i, wName, depName, headName, salary);
			i++;
		}
	}

	private static boolean isItDep(Worker<?> worker) {
		return worker.getDep().getClass().toString().equalsIgnoreCase("class Department");

	}

	private static ArrayList<Worker<?>> createNewWorkersList(String str) {
		ArrayList<Worker<?>> workersList = new ArrayList<Worker<?>>();
		boolean isUserPressed1 = (str.compareTo("1") == 0);
		// SE = "Software Engineering"
		workersList.add(new Worker<>("Elvis", isUserPressed1 ? new Department(SE, BOSS[1]) : SE, 1000));
		// ME = "Mechanical Engineering"
		workersList.add(new Worker<>("Samba", isUserPressed1 ? new Department(ME, BOSS[2]) : ME, 2000));
		// IME = "Industrial & Mechanical Engineering"
		workersList.add(new Worker<>("Bamba", isUserPressed1 ? new Department(IME, BOSS[3]) : IME, 3000));
		// ELE = "Electrical Engineering"
		workersList.add(new Worker<>("Bisli", isUserPressed1 ? new Department(ELE, BOSS[4]) : ELE, 4000));
		workersList.add(new Worker<>("Kinder Bueno", isUserPressed1 ? new Department(ELE, BOSS[4]) : ELE, 1000));
		return workersList;
	}

	private static void save(ArrayList<Worker<?>> arrayOfWorkers) throws FileNotFoundException, IOException {
		try (DataOutputStream o1 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(WORKERS_FILE)))) {
			boolean isItDep = isItDep(arrayOfWorkers.get(0));
			if (isItDep)
				o1.writeByte(1);
			else
				o1.writeByte(0);
			for (Worker<?> worker : arrayOfWorkers) {
				worker.writeWorker(o1, isItDep);
			}
		}

		try (DataOutputStream o2 = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(NAMES_FILE)))) {
			for (Worker<?> worker : arrayOfWorkers) {
				String wName = worker.getName();
				FixedLengthStringIO.writeFixedLengthString(wName, MAXIMUM_CHARS_IN_WORKER_NAME, o2);
			}
		}
	}

}
