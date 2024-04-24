/**
 * The Proposer class represents a component responsible for proposing operations in a distributed consensus protocol.
 * It extends the functionality of the KVStore class and implements the Runnable interface.
 */
package Server;

import Compute.IKVStore;
import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class Proposer extends KVStore implements Runnable {

	/** The logger instance for logging messages. */
	private final Logger logger = new Logger();

	/** The current proposal identifier. */
	private static int proposalId;

	/** The value to be proposed. */
	private int value;

	/**
	 * Constructs a Proposer object.
	 */
	public Proposer() {
		super();
	}

	/**
	 * Sets the value to be proposed.
	 * @param value The value to be proposed.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/** Starts the proposer. */
	public void start() {
		proposalId = 0;
	}

	/**
	 * Proposes an operation in the distributed consensus protocol.
	 * @param key The key involved in the operation.
	 * @param value The value associated with the key.
	 * @param reqType The type of request (1 for get, 2 for put, 3 for delete).
	 * @return A message indicating the success or failure of the proposed operation.
	 */
	public synchronized String propose(int key, int value, int reqType) {
		proposalId++;
		setValue(key);
		Map<String, String> serverMap = ServerStoreUtil.getServerMap();

		// Try to prepare
		int countPrepare = attemptPhase("prepare", serverMap, proposalId, key, value, reqType);
		if (countPrepare <= Constants.NUMBER_OF_SERVERS / 2) {
			return logAndReturnErrorResponse(countPrepare);
		}

		// Try to accept
		int countAccept = attemptPhase("accept", serverMap, proposalId, key, value, reqType);
		if (countAccept <= Constants.NUMBER_OF_SERVERS / 2) {
			return logAndReturnErrorResponse(countAccept);
		}

		// Try to commit
		return attemptCommit(serverMap, key, value, reqType);
	}

	private int attemptPhase(String phase, Map<String, String> serverMap, int proposalId, int key, int value, int reqType) {
		int count = 0;
		for (Map.Entry<String, String> entry : serverMap.entrySet()) {
			try {
				Registry registry = LocateRegistry.getRegistry(entry.getValue(), ServerStoreUtil.getPortNumber(entry.getKey()));
				IKVStore stub = (IKVStore) registry.lookup(entry.getKey());
				if ((phase.equals("prepare") && stub.prepare(proposalId, key, value, reqType)) ||
						(phase.equals("accept") && stub.accept(proposalId, key, value, reqType))) {
					count++;
				}
			} catch (SocketTimeoutException | RemoteException | NotBoundException ignored) {
				// continue to next server
			}
		}
		logger.ackRequestReceived(phase.toUpperCase(), count);
		return count;
	}

	private String attemptCommit(Map<String, String> serverMap, int key, int value, int reqType) {
		String response = "";
		for (Map.Entry<String, String> entry : serverMap.entrySet()) {
			try {
				Registry registry = LocateRegistry.getRegistry(entry.getValue(), ServerStoreUtil.getPortNumber(entry.getKey()));
				IKVStore stub = (IKVStore) registry.lookup(entry.getKey());
				response = stub.commit(key, value, reqType);  // Assuming last valid response is acceptable
			} catch (SocketTimeoutException | RemoteException | NotBoundException ignored) {
				// continue to next server
			}
		}
		return response;
	}

	private String logAndReturnErrorResponse(int count) {
		String response = logger.errResponse(count);
		logger.paxosLog(response);
		return response;
	}

	@Override
	public void run() {
		// Implementation here if needed
	}
}
