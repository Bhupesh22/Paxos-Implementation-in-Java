/**
 * The Server5 class represents an instance of a server in the key-value store system.
 * It extends the IKVStoreServer class and acts as a server implementation.
 */
package Server;

import Compute.IKVStore;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server5 extends IKVStoreServer {

	/**
	 * The main method to start the Server5 instance.
	 * @param args Command-line arguments (not used).
	 * @throws Exception If an error occurs during server initialization.
	 */
	public static void main(String args[]) throws Exception {
		Logger logger = new Logger();
		try {
			Server5 server = new Server5(5);
			IKVStore stub = (IKVStore) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(Constants.SERVER5_PORT_NO);
			registry.bind(Constants.SERVER5, stub);
			System.out.println("Server5 is running...");
		} catch (Exception e) {
			logger.connectionException();
		}
	}

	/**
	 * Constructs a Server5 object with the specified server number.
	 * @param serverNumber The server number.
	 * @throws RemoteException If a remote communication error occurs.
	 */
	public Server5(int serverNumber) throws RemoteException {
		super(serverNumber);
	}

}
