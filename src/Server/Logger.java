package Server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public String getCurrentTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        return "<Time: " + simpleDateFormat.format(new Date()) + ">";
    }

    // Log a message with a timestamp
    private void logMessage(String message) {
        System.out.println(getCurrentTimeStamp() + " " + message);
    }

    // Log an informational message
    private void logInfo(String message) {
        logMessage("[INFO] " + message);
    }

    // Log an error message
    private void logError(String message) {
        logMessage("[ERROR] " + message);
    }

    // Log a server shutdown error
    public void errServerDown(int serverNumber) {
        logInfo("Server " + serverNumber + " has shut down for maintenance.");
    }

    // Log the acknowledgment of receiving a request from the client
    public void ackRequestSent(String request) {
        logInfo("Client sent a request: \"" + request + "\" to the server.");
    }

    // Log the acknowledgment of receiving a request from the client
    public void ackRequestReceived(String stage, int count) {
        logInfo("Paxos: " + count + " servers have positively replied for the \"" + stage + "\" stage.");
    }

    // Log a consensus error message
    public String errResponse(int count) {
        return "[INFO] " + count + " servers have replied but did not reach consensus.";
    }

    // Log a generic message for Paxos protocol
    public void paxosLog(String message) {
        logInfo("Paxos: " + message);
    }

    // Log the acknowledgment of a successful PUT operation
    public String ackPut(int key, int value) {
        return "[INFO] Successfully stored key \"" + key + "\" with value \"" + value + "\".";
    }

    // Log an error for a failed PUT operation
    public String errPut(int key, int value) {
        return "[INFO] The key already exists. Failed to store key \"" + key + "\" with value \"" + value + "\".";
    }

    // Log the acknowledgment of a successful GET operation
    public String ackGet(int key, int value) {
        return "[INFO] Found value \"" + value + "\" for key \"" + key + "\" in the store.";
    }

    // Log an error for a failed GET operation
    public String errGet() {
        return "[INFO] Key does not exist.";
    }

    // Log the acknowledgment of a successful DELETE operation
    public String ackDel(int key, int value) {
        return "[INFO] Removed key \"" + key + "\" with value \"" + value + "\" from the store.";
    }

    // Log an error for a failed DELETE operation
    public String errDel(int key) {
        return "[INFO] Key does not exist.";
    }

    // Log an error for an illegal request type
    public void illegalRequestTypeException() {
        logError("Received an invalid request from the user. Please check and try again.");
    }

    // Log an error for a connection exception
    public void connectionException() {
        logError("Connection exception or RMI error occurred. Please check and try again.");
    }
}
