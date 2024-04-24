/**
 * The Client class represents a client application that interacts with a remote server through RMI.
 * It provides functionalities to perform operations like PUT, GET, and DEL on a key-value store service.
 */
package Client;

import Compute.IKVStore;
import Server.Constants;
import Server.Logger;
import Server.ServerStoreUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Client {

	private static final String QUIT_COMMAND = "quit";
	private static final Set<String> VALID_OPERATIONS = new HashSet<>(Arrays.asList("PUT", "GET", "DEL"));

	/**
	 * Main method to start the client application.
	 * @param args Command-line arguments specifying the server to connect to.
	 */
	public static void main(String args[]) {
		Logger logger = new Logger();
		System.out.println("Client has started ....");

		try {
			if (SERVERS.contains(args[0])) {
				Registry registry = LocateRegistry.getRegistry("localhost", ServerStoreUtil.getPortNumber(args[0]));
				IKVStore stub = (IKVStore) registry.lookup(args[0]);
				populateKV(stub);
				performOperation(stub);
			} else {
				System.out.println("Invalid Argument");
			}

		} catch (RemoteException | NotBoundException e) {
			logger.connectionException();
			return;
		}
	}

	/**
	 * Performs operations like PUT, GET, DEL on the remote server.
	 * @param stub The remote service stub to perform operations on.
	 */
	private static void performOperation(IKVStore stub) {
		Scanner scanner = new Scanner(System.in);
		Logger logger = new Logger();

		boolean flag = true;
		while (flag) {
			System.out.println("================= Operations ============================");
			System.out.println("Enter Operation, Key, value (or 'quit' to exit):\n" +
					"Syntax for PUT: <PUT> <KEY> <VALUE>\n" +
					"Syntax for GET or DEL: <GET/DEL> <KEY>\n" +
					"Example: -----------------------------------\n" +
					"PUT 100 188 \nGET 100 \nDEL 100\n--------------------------------------");

			String input = scanner.nextLine().trim();
			if (input.equalsIgnoreCase(QUIT_COMMAND)) {
				flag = false;
				continue;
			}

			String[] formattedInput = input.split(" ");
			if (isValidInput(formattedInput)) {
				try {
					processOperation(stub, formattedInput[0].toUpperCase(), formattedInput[1], formattedInput.length == 3 ? formattedInput[2] : "");
				} catch (Exception e) {
					logger.illegalRequestTypeException();
				}
			} else {
				logger.illegalRequestTypeException();
			}
			printSeparatorLine();
		}
		scanner.close();
	}

	/**
	 * Takes input from the command line.
	 * @return Input string entered in the terminal.
	 * @throws IOException If an I/O error occurs.
	 */
	public static String getInputFromTerminal() throws IOException {
		BufferedReader stringIn = new BufferedReader(new InputStreamReader(System.in));
		return stringIn.readLine();
	}

	/**
	 * Prints a separator line.
	 */
	private static void printSeparatorLine() {
		System.out.println("===================================================================================");
	}

	/**
	 * Populates the key-value store with predefined key-value pairs.
	 * @param stub The remote service stub to populate data into.
	 * @throws RemoteException If a remote communication error occurs.
	 */
	private static void populateKV(IKVStore stub) throws RemoteException {

		String[][] operations = {
				{"PUT", "100", "1"}, {"PUT", "200", "2"}, {"PUT", "300", "3"}, {"PUT", "400", "4"}, {"PUT", "500", "5"},
				{"GET", "100", ""}, {"GET", "200", ""}, {"GET", "300", ""}, {"GET", "400", ""}, {"GET", "500", ""},
				{"DEL", "100", ""}, {"DEL", "200", ""}, {"DEL", "300", ""}, {"DEL", "400", ""}, {"DEL", "500", ""},
				{"PUT", "1", "1"}, {"PUT", "2", "2"}, {"PUT", "3", "3"}, {"PUT", "4", "4"}, {"PUT", "5", "5"}
		};

		for (String[] op : operations) {
			processOperation(stub, op[0], op[1], op[2]);
		}
	}

	/**
	 * Processes the specified operation on the key-value store.
	 * @param stub The remote service stub to perform the operation on.
	 * @param action The action to perform (PUT, GET, DEL).
	 * @param k The key for the operation.
	 * @param v The value for the operation (if applicable).
	 * @throws RemoteException If a remote communication error occurs.
	 */
	private static void processOperation(IKVStore stub, String action, String k, String v) throws RemoteException {
		Logger logger = new Logger();
		int key;
		try {
			key = Integer.parseInt(k);
		} catch (NumberFormatException e) {
			logger.ackRequestSent("Invalid key format. Key must be an integer.");
			return; // Exit the method if key format is invalid
		}

		switch (action) {
			case "PUT":
				int value = 0;
				try {
					value = Integer.parseInt(v);
				} catch (NumberFormatException e) {
					logger.ackRequestSent("Invalid value format. Value must be an integer.");
					return; // Exit the method if value format is invalid
				}
				logger.ackRequestSent(String.format("Put operation (Key, Value) -> (%d, %d)", key, value));
				System.out.println(stub.put(key, value));
				break;
			case "GET":
				logger.ackRequestSent(String.format("Get operation (Key) -> (%d)", key));
				System.out.println(stub.get(key));
				break;
			case "DEL":
				logger.ackRequestSent(String.format("Delete operation (Key) -> (%d)", key));
				System.out.println(stub.delete(key));
				break;
			default:
				logger.ackRequestSent("Invalid Operation");
				break;
		}
	}

	private static final Set<String> SERVERS = new HashSet<>(Arrays.asList(
			Constants.SERVER1, Constants.SERVER2, Constants.SERVER3, Constants.SERVER4, Constants.SERVER5
	));

	/**
	 * Checks if the input command is valid.
	 * @param input The input command.
	 * @return True if the input is valid, false otherwise.
	 */
	private static boolean isValidInput(String[] input) {
		if (input.length < 2 || input.length > 3) {
			return false;
		}
		if (!VALID_OPERATIONS.contains(input[0].toUpperCase())) {
			return false;
		}
		return true;
	}
}
