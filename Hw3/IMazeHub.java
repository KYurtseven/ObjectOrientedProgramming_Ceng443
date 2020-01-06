
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMazeHub extends Remote {
	
	/**
	 * Creates a maze with given parameters
	 * @param width width of the maze
	 * @param height height of the maze
	 * @throws RemoteException remote exception
	 */
    void createMaze(int width, int height) throws RemoteException;
    
    /**
     * 
     * @param index maze's index in the hub
     * @return maze at given index
     * @throws RemoteException remote exception
     */
    IMaze getMaze(int index) throws RemoteException;
    
    /**
     * 
     * @param index maze's index in the hub
     * @return true on successful deletion, false otherwise
     * @throws RemoteException remote exception
     */
    boolean removeMaze(int index) throws RemoteException;
}
