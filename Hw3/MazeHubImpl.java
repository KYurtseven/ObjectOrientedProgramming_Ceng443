import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MazeHubImpl extends UnicastRemoteObject implements IMazeHub{

	ArrayList<IMaze> mazeList;
	
	/**
	 * @throws RemoteException
	 */
	protected MazeHubImpl() throws RemoteException {
		// This line does not do anything since the 
		// super() is always called
		super();
		this.mazeList = new ArrayList<>();
	}

	/* (non-Javadoc)
	 * @see IMazeHub#createMaze(int, int)
	 */
	@Override
	public void createMaze(int width, int height) throws RemoteException {
		// Create the maze
		IMaze maze = new MazeImpl();
		maze.create(height, width);
		// Store the maze
		mazeList.add(maze);
	}

	/* (non-Javadoc)
	 * @see IMazeHub#getMaze(int)
	 */
	@Override
	public IMaze getMaze(int index) throws RemoteException {
		try {
			return mazeList.get(index);
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
		catch(Exception e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see IMazeHub#removeMaze(int)
	 */
	@Override
	public boolean removeMaze(int index) throws RemoteException {
		try {
			mazeList.remove(mazeList.get(index));
			return true;
		}
		catch(IndexOutOfBoundsException e) {
			return false;
		}
		catch(Exception e) {
			return false;
		}
	}

}
