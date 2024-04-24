### Overview

This program builds upon RMI for client-server connections as well as Paxos operations. In the Paxos implementation, there are three entities/classes:

**Proposer:**  
Receive requests from client and try to convince acceptors to accept the proposed values.

**Acceptor:**  
Accept certain proposed values from proposers and let proposers know if something else was accepted. A response from an acceptor represents a vote for a particular proposal.

**Learner:**  
Announce the outcome and commit the final result.

During each request process, two phases are involved:

- Basically, the proposer interacts with the acceptors twice.
- In Phase 1, a proposer asks all the working acceptors whether anyone already received a proposal. If the answer is no, propose a value.
- In Phase 2, if a majority of acceptors agree to this value then that is consensus.

### Technical Impression

**Acceptor:**

The Acceptor class represents a component responsible for accepting or rejecting proposals in a distributed consensus protocol. It maintains state information such as the last accepted proposal identifier (lastAcceptedProposalId) and a failure counter to simulate fault tolerance. Here's a breakdown of its functionality:
- The accept method is responsible for accepting a proposal. It checks if the proposal is valid and handles faults by simulating server failures.
- The prepare method prepares for a distributed transaction by checking the validity of the proposal and handling faults similarly to the accept method.
- The handleFaults method simulates faults by randomly shutting down servers and implements fault tolerance mechanisms.
- The Acceptor runs as a separate thread (Runnable) to handle concurrent requests.

**Proposer:**

The Proposer class represents a component responsible for proposing operations in a distributed consensus protocol. It interacts with Acceptor instances to reach consensus. Here's a breakdown of its functionality:
- The propose method proposes an operation in the distributed consensus protocol. It goes through prepare, accept, and commit phases, interacting with Acceptor instances and handling responses.
- The attemptPhase method attempts either the prepare or accept phase by interacting with Acceptor instances and handling responses.
- The attemptCommit method attempts the commit phase by interacting with Learner instances and handling responses.
- The Proposer also runs as a separate thread (Runnable) to handle concurrent proposals.

**Learner:**
The Learner class represents a component responsible for committing proposed operations. It listens for responses from Proposers and Acceptors and commits operations to the key-value store. Here's a breakdown of its functionality:
- The commit method commits a proposed operation to the key-value store based on the request type (GET, PUT, DELETE).
- The Learner class runs as a separate thread (Runnable) to handle concurrent commit operations.



### Steps to run

T### Steps to run

There are two ways to run the program

1. using .bat file
    - compile using `compile_script.bat`
    - execute using `run_script.bat`

2. using java commands

    - Navigate to src folder, compile the code using

            javac Client/*.java

            javac Compute/*.java
   
            javac Server/*java
   - Now to execute the code

    **Server**
    
     ```
     java Server/Server1
     java Server/Server2
     java Server/Server3
     java Server/Server4
     java Server/Server5
     ```
    
    **Client**
    
     ```
     java Client/Client localhost Server1
     ```

   