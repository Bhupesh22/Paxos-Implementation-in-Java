/**
 * The KVStore class represents a simple key-value store.
 */
package Server;

import java.util.HashMap;

public class KVStore {

	/** The map storing key-value pairs. */
	private static HashMap<Integer, Integer> keyValueMap;

	/** The logger instance for logging messages. */
	private final Logger logger = new Logger();

	/**
	 * Constructs a KVStore object and initializes the key-value map.
	 */
	public KVStore() {
		keyValueMap = new HashMap<>();
	}

	/**
	 * Puts a key-value pair into the store.
	 * @param key The key.
	 * @param value The value.
	 * @return A message indicating the success or failure of the put operation.
	 */
	public String putKey(int key, int value) {
		String response = keyValueMap.containsKey(key) ? logger.errPut(key, value) : logger.ackPut(key, value);
		System.out.println(response);
		return keyValueMap.putIfAbsent(key, value) == null ? response : logger.errPut(key, value);
	}

	/**
	 * Retrieves the value associated with a key from the store.
	 * @param key The key.
	 * @return A message indicating the success or failure of the get operation.
	 */
	public String getKey(int key) {
		String response = keyValueMap.containsKey(key) ? logger.ackGet(key, keyValueMap.get(key)) : logger.errGet();
		System.out.println(response);
		return response;
	}

	/**
	 * Deletes a key-value pair from the store.
	 * @param key The key.
	 * @return A message indicating the success or failure of the delete operation.
	 */
	public String deleteKey(int key) {
		String response = keyValueMap.containsKey(key) ? logger.ackDel(key, keyValueMap.remove(key)) : logger.errDel(key);
		System.out.println(response);
		return response;
	}

	/**
	 * Checks if a request is valid based on the key and action.
	 * @param key The key.
	 * @param action The action (1 for get, 2 for put, 3 for delete).
	 * @return True if the request is valid, false otherwise.
	 */
	public boolean checkReq(int key, int action) {
		switch (action) {
			case 1: return keyValueMap.containsKey(key); // get
			case 2: return !keyValueMap.containsKey(key); // put
			case 3: return keyValueMap.containsKey(key); // delete
			default: return false;
		}
	}
}
