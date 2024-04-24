/**
 * The Server4 class represents an instance of a server in the key-value store system.
 * It extends the IKVStoreServer class and acts as a server implementation.
 */
package Server;

import Compute.IKVStore;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server4 extends IKVStoreServer {

	/**
	 * The main method to start the Server4 instance.
	 * @param args Command-line arguments (not used).
	 * @throws Exception If an error occurs during server initialization.
	 */
	public static void main(String args[]) throws Exception {
		Logger logger = new Logger();
		try {
			Server4 server = new Server4(4);
			IKVStore stub = (IKVStore) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(Constants.SERVER4_PORT_NO);
			registry.bind(Constants.SERVER4, stub);
			System.out.println("Server4 is running...");
		} catch (Exception e) {
			logger.connectionException();
		}
	}

	/**
	 * Constructs a Server4 object with the specified server number.
	 * @param serverNumber The server number.
	 * @throws RemoteException If a remote communication error occurs.
	 */
	public Server4(int serverNumber) throws RemoteException {
		super(serverNumber);
	}

}
