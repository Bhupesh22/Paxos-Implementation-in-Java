/**
 * The Learner class represents a component responsible for committing proposed operations.
 * It extends the functionality of the KVStore class and implements the Runnable interface.
 */
package Server;

public class Learner extends KVStore implements Runnable {

	/**
	 * Commits a proposed operation.
	 * @param key The key involved in the operation.
	 * @param value The value associated with the key (for put operation).
	 * @param reqType The type of request (1 for get, 2 for put, 3 for delete).
	 * @return A message indicating the success or failure of the commit operation.
	 */
	public String commit(int key, int value, int reqType) {
		String response = "";
		switch (reqType) {
			case 1:
				response = super.getKey(key); // get
				break;
			case 2:
				response = super.putKey(key, value); // put
				break;
			case 3:
				response = super.deleteKey(key); // delete
				break;
		}
		return response;
	}

	/** Starts the learner. */
	public void start() {}

	@Override
	public void run() {}
}
