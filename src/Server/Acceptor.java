/**
 * The Acceptor class represents a component responsible for accepting or rejecting proposals
 * in a distributed consensus protocol.
 */
package Server;

import java.net.SocketTimeoutException;
import java.rmi.RemoteException;

public class Acceptor extends KVStore implements Runnable {

	private final Logger logger = new Logger();

	private static int failureCounter;
	private static int lastAcceptedProposalId;
	private boolean isActive;
	private int serverNumber;

	/**
	 * Sets the proposal identifier for this acceptor.
	 * @param lastAcceptedProposalId The last accepted proposal identifier to set.
	 */
	public void setLastAcceptedProposalId(int lastAcceptedProposalId) {
		Acceptor.lastAcceptedProposalId = lastAcceptedProposalId;
	}

	/**
	 * Starts the acceptor.
	 */
	public void start() {
		isActive = true;
	}

	/**
	 * Accepts a proposal.
	 * @param proposalId The proposal identifier.
	 * @param key The key involved in the proposal.
	 * @param value The value associated with the key.
	 * @param requestType The type of request (e.g., PUT, GET, DEL).
	 * @return True if the proposal is accepted, false otherwise.
	 * @throws RemoteException If a remote communication error occurs.
	 * @throws SocketTimeoutException If a socket timeout occurs during communication.
	 */
	public boolean accept(int proposalId, int key, int value, int requestType) throws RemoteException, SocketTimeoutException {
		return handleFaults(proposalId, key, value, requestType);
	}

	/**
	 * Prepares for a distributed transaction.
	 * @param proposalId The proposal identifier.
	 * @param key The key involved in the proposal.
	 * @param value The value associated with the key.
	 * @param requestType The type of request (e.g., PUT, GET, DEL).
	 * @return True if the preparation is successful, false otherwise.
	 * @throws RemoteException If a remote communication error occurs.
	 * @throws SocketTimeoutException If a socket timeout occurs during communication.
	 */
	public boolean prepare(int proposalId, int key, int value, int requestType) throws RemoteException, SocketTimeoutException {
		return handleFaults(proposalId, key, value, requestType);
	}

	private boolean handleFaults(int proposalId, int key, int value, int requestType) {
		try {
			if (failureCounter % 20 == 0) {
				logger.errServerDown(serverNumber);
				Thread.sleep(200);
			}
			failureCounter++;
		} catch (InterruptedException ie) {
			// Do nothing if interrupted
		}
		if (proposalId < lastAcceptedProposalId) {
			return false;
		}
		if (super.checkReq(key, requestType)) {
			setLastAcceptedProposalId(proposalId);
			return true;
		}
		return false;
	}

	/**
	 * Sets the server number for this acceptor.
	 * @param serverNumber The server number to set.
	 */
	public void setServerNumber(int serverNumber) {
		this.serverNumber = serverNumber;
	}

	@Override
	public void run() {
		// Implementation of the run method
	}
}
