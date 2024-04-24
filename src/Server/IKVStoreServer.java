/**
 * The IKVStoreServer class represents a server implementation of the IKVStore interface.
 * It acts as a coordinator between the Proposer, Learner, and Acceptor components.
 */
package Server;

import Compute.IKVStore;

import java.net.SocketTimeoutException;
import java.rmi.RemoteException;

public class IKVStoreServer implements IKVStore {

	private Proposer proposer;
	private Learner learner;
	private Acceptor acceptor;

	/**
	 * Constructs an IKVStoreServer object with the specified server number.
	 * @param serverNumber The number of the server.
	 */
	public IKVStoreServer(int serverNumber) {
		proposer = new Proposer();
		learner = new Learner();
		acceptor = new Acceptor();
		proposer.start();
		learner.start();
		acceptor.start();
		acceptor.setServerNumber(serverNumber);
	}

	/**
	 * Retrieves the value associated with the specified key.
	 * @param key The key to retrieve the value for.
	 * @return The value associated with the key.
	 */
	public String get(int key) {
		return proposer.propose(key, Integer.MIN_VALUE, 1);
	}

	/**
	 * Associates the specified value with the specified key in the key-value store.
	 * @param key The key to associate the value with.
	 * @param value The value to be associated with the key.
	 * @return A message indicating the success or failure of the operation.
	 */
	public String put(int key, int value) {
		return proposer.propose(key, value, 2);
	}

	/**
	 * Deletes the key-value pair associated with the specified key.
	 * @param key The key of the pair to be deleted.
	 * @return A message indicating the success or failure of the operation.
	 */
	public String delete(int key) {
		return proposer.propose(key, Integer.MIN_VALUE, 3);
	}

	@Override
	public boolean prepare(int proposalId, int key, int value, int action) throws RemoteException, SocketTimeoutException {
		return acceptor.prepare(proposalId, key, value, action);
	}

	@Override
	public boolean accept(int proposalId, int key, int value, int action) throws RemoteException, SocketTimeoutException {
		return acceptor.accept(proposalId, key, value, action);
	}

	@Override
	public String commit(int key, int value, int action) throws RemoteException, SocketTimeoutException {
		return learner.commit(key, value, action);
	}
}
