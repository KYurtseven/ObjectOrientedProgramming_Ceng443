
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMaze extends Remote {
		
	/**
	 * Generates maze with given parameters
	 * @param height height
	 * @param width width
	 * @throws RemoteException remote exception
	 */
    void create(int height, int width) throws RemoteException;
    
    /**
     * Finds maze object in the field
     * @param position
     * @return object if it exists, null if there are no objects
     * @throws RemoteException remote exception
     */
    MazeObject getObject(Position position) throws RemoteException;
    
    
    /**
     * Creates an object at given position
     * The object type is either an agent, or other maze objects
     * @param position position
     * @param type agent or some kind of maze object
     * @return true on successful creation, false if there are objects at given location
     * @throws RemoteException remote exception
     */
    boolean createObject(Position position, MazeObjectType type) throws RemoteException;
    
    /**
     * Deletes the object from the grid
     * @param position position
     * @return true on successful deletion, false on there is no object at that location
     * @throws RemoteException remote exception
     */
    boolean deleteObject(Position position) throws RemoteException;
    
    /**
     * 
     * @return agents in the grid
     * @throws RemoteException remote exception
     */
    Agent[] getAgents() throws RemoteException;
    
    /**
     * Moves agent to the next position
     * @param id id of the agent
     * @param position next position
     * @return true on successful, false otherwise
     * @throws RemoteException remote exception
     */
    boolean moveAgent(int id, Position position) throws RemoteException;
    
    /**
     * 
     * @return human readable string version of the grid
     * @throws RemoteException remote exception
     */
    String print() throws RemoteException;
}
