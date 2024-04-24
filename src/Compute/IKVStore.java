/**
 * The IKVStore interface represents the remote service interface for key-value store operations.
 * It defines methods for getting, putting, and deleting key-value pairs, as well as methods for
 * coordinating distributed transactions.
 */
package Compute;

import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IKVStore extends Remote {

	/**
	 * Retrieves the value associated with the specified key.
	 * @param key The key to retrieve the value for.
	 * @return The value associated with the key.
	 * @throws RemoteException If a remote communication error occurs.
	 */
	String get(int key) throws RemoteException;

	/**
	 * Associates the specified value with the specified key in the key-value store.
	 * @param key The key to associate the value with.
	 * @param value The value to be associated with the key.
	 * @return A message indicating the success or failure of the operation.
	 * @throws RemoteException If a remote communication error occurs.
	 */
	String put(int key, int value) throws RemoteException;

	/**
	 * Deletes the key-value pair associated with the specified key.
	 * @param key The key of the pair to be deleted.
	 * @return A message indicating the success or failure of the operation.
	 * @throws RemoteException If a remote communication error occurs.
	 */
	String delete(int key) throws RemoteException;

	/**
	 * Prepares the server for a distributed transaction.
	 * @param proposalId The proposal identifier for the transaction.
	 * @param key The key involved in the transaction.
	 * @param value The value associated with the key.
	 * @param action The action to be performed (e.g., PUT, GET, DEL).
	 * @return True if the server is prepared for the transaction, false otherwise.
	 * @throws RemoteException If a remote communication error occurs.
	 * @throws SocketTimeoutException If a socket timeout occurs during communication.
	 */
	boolean prepare(int proposalId, int key, int value, int action) throws RemoteException, SocketTimeoutException;

	/**
	 * Accepts the proposal for a distributed transaction.
	 * @param proposalId The proposal identifier for the transaction.
	 * @param key The key involved in the transaction.
	 * @param value The value associated with the key.
	 * @param action The action to be performed (e.g., PUT, GET, DEL).
	 * @return True if the server accepts the transaction, false otherwise.
	 * @throws RemoteException If a remote communication error occurs.
	 * @throws SocketTimeoutException If a socket timeout occurs during communication.
	 */
	boolean accept(int proposalId, int key, int value, int action) throws RemoteException, SocketTimeoutException;

	/**
	 * Commits a distributed transaction.
	 * @param key The key involved in the transaction.
	 * @param value The value associated with the key.
	 * @param action The action to be performed (e.g., PUT, GET, DEL).
	 * @return A message indicating the success or failure of the commit operation.
	 * @throws RemoteException If a remote communication error occurs.
	 * @throws SocketTimeoutException If a socket timeout occurs during communication.
	 */
	String commit(int key, int value, int action) throws RemoteException, SocketTimeoutException;

}
