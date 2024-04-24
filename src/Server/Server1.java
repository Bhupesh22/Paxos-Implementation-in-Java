/**
 * The Server1 class represents an instance of a server in the key-value store system.
 * It extends the IKVStoreServer class and acts as a server implementation.
 */
package Server;

import Compute.IKVStore;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server1 extends IKVStoreServer {

	/**
	 * The main method to start the Server1 instance.
	 * @param args Command-line arguments (not used).
	 * @throws Exception If an error occurs during server initialization.
	 */
	public static void main(String args[]) throws Exception {
		Logger logger = new Logger();
		try {
			Server1 server = new Server1(1);
			IKVStore stub = (IKVStore) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(Constants.SERVER1_PORT_NO);
			registry.bind(Constants.SERVER1, stub);
			System.out.println("Server1 is running...");
		} catch (Exception e) {
			logger.connectionException();
		}
	}

	/**
	 * Constructs a Server1 object with the specified server number.
	 * @param serverNumber The server number.
	 * @throws RemoteException If a remote communication error occurs.
	 */
	public Server1(int serverNumber) throws RemoteException {
		super(serverNumber);
	}
}
