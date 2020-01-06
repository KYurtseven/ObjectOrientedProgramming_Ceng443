import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import sun.management.resources.agent;

/**
 * Implementation of IMaze interface
 * It extends UnicastRemoteObject
 * 
 * @author koray
 *
 */
public class MazeImpl extends UnicastRemoteObject implements IMaze{

	
	private int height;
	private int width;
	private int agentCount;
	private MazeObject[][] grid;
	private ArrayList<Agent> agentList;
	
	/**
	 * Default constructor because of the UnicastRemoteObject
	 * @throws RemoteException remote exception
	 */
	public MazeImpl() throws RemoteException {
		// This line does not do anything since the 
		// super() is always called
		super();
	}

	
	/* (non-Javadoc)
	 * @see IMaze#create(int, int)
	 */
	public void create(int height, int width) throws RemoteException {		
		this.height = height;
		this.width = width;
		grid = new MazeObject[height][width];
		agentList = new ArrayList<>();
		this.agentCount = 0;
	}

	
	/* (non-Javadoc)
	 * @see IMaze#getObject(Position)
	 */
	public MazeObject getObject(Position position) throws RemoteException {
		try {
			return grid[position.getY()][position.getX()];
		}
		catch(Exception e) {
			return null;
		}
	}

	
	/* (non-Javadoc)
	 * @see IMaze#createObject(Position, MazeObjectType)
	 */
	public boolean createObject(Position position, MazeObjectType type) throws RemoteException {
		
		if(getObject(position) == null) {
			if(type.equals(MazeObjectType.AGENT)) {
				Agent agent = new Agent(position, agentCount);
				// place the agent to the grid
				grid[position.getY()][position.getX()] = agent;
				// add the agent to the agent list for helping method getAgents
				agentList.add(agent);
				// increase the total agentCount for next agents.
				agentCount++;
				// object is created
				return true;
			}
			else {
				MazeObject mazeObject = new MazeObject(position, type);
				// place the maze object to the grid
				grid[position.getY()][position.getX()] = mazeObject;
				return true;
			}
		}
		// there is an element in the position
		return false;
	}

	
	/* (non-Javadoc)
	 * @see IMaze#deleteObject(Position)
	 */
	public boolean deleteObject(Position position) throws RemoteException {
		MazeObject toBeDeletedObj = getObject(position);
		if(toBeDeletedObj != null) {
			// remove from the grid
			grid[position.getY()][position.getX()] = null;
			// if it is agent, remove from agent list too
			if(toBeDeletedObj.getType().equals(MazeObjectType.AGENT)) {
				agentList.remove((Agent) toBeDeletedObj);
			}
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see IMaze#getAgents()
	 */
	public Agent[] getAgents() throws RemoteException {
		// We cannot use agentCount, because some of them might be dead
		// Instead, use agentList.size()
		// That list always have the active (on the grid) agents
		int size = agentList.size();
		Agent[] agentArray = new Agent[size];
		for(int i = 0; i < size; i++) {
			agentArray[i] = agentList.get(i);
		}
		return agentArray;
	}

	/* (non-Javadoc)
	 * @see IMaze#moveAgent(int, Position)
	 */
	public boolean moveAgent(int id, Position position) throws RemoteException {
		
		// find agent to be moved
		Agent agent = findAgentToBeMoved(id);
		// invalid id
		if(agent == null) {
			return false;
		}
		// move only if the position difference is 1
		// i.e. allow only up, down, right, lefts
		if(agent.getPosition().distance(position) == 1) {
			// Check the new position for cases
			MazeObject mazeObject = getObject(position);
			
			// Case 1
			// There is no object in the next position
			if(mazeObject == null) {
				moveAgentToNullOrGold(agent, position, false);
				return true;
			}
			// Case 2
			// There is hole in the next position
			else if(mazeObject.getType().equals(MazeObjectType.HOLE)) {
				// Remove agent from the list
				agentList.remove(agent);
				// Remove agent from the grid
				grid[agent.getPosition().getY()][agent.getPosition().getX()] = null;
				return true;
			}
			// Case 3
			// Another agent or a wall in the next position
			else if(mazeObject.getType().equals(MazeObjectType.AGENT) ||
				mazeObject.getType().equals(MazeObjectType.WALL)) {
				return false;
			}
			// Case 4
			// Gold in the next position
			else if(mazeObject.getType().equals(MazeObjectType.GOLD)) {
				moveAgentToNullOrGold(agent, position, true);
				return true;
			}
			// Invalid case?
			else {
				return false;
			}
			
		}
		// The distance is not 1
		return false;
	}

	/**
	 * Helper method of moveAgent.
	 * Moves agent either to an empty place
	 * or over the gold
	 * @param agent agent to be moved
	 * @param isGold true is gold, false is null
	 * 
	 */
	private void moveAgentToNullOrGold(Agent agent, Position position, boolean isGold) {
		
		// Increase the gold count to collect
		if(isGold) {
			agent.increaseCollectedGold();
		}
		// remove from the previous position
		grid[agent.getPosition().getY()][agent.getPosition().getX()] = null;
		// move agent to the next position
		agent.setPosition(position);
		// move in the grid
		grid[position.getY()][position.getX()] = agent;
		
	}
	
	/**
	 * Helper method of moveAgent
	 * Finds agent with given id in the list
	 * @param id
	 * @return
	 */
	private Agent findAgentToBeMoved(int id) {
		for(Agent agent: agentList) {
			if(agent.getId() == id) {
				return agent;
			}
		}
		return null;
	}
		
	/* (non-Javadoc)
	 * @see IMaze#print()
	 */
	public String print() throws RemoteException {
		// Rather than string, using modifiable string types
		// might be a good idea
		String str = "";
		str += "+";
		for(int i = 0; i < grid[0].length; i++) {
			str += "#";
		}
		str += "+\n";
		
		for(int i = 0; i < grid.length; i++) {
			str += "|";
			for(int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] == null) {
					str += " ";
				}
				else {
					str += grid[i][j].toString();
				}
			}
			str += "|\n";
		}
		str += "+";
		for(int i = 0; i < grid[0].length; i++) {
			str += "#";
		}
		str += "+\n";
		
		return str;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}


	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the mazeObjects
	 */
	public MazeObject[][] getMazeObjects() {
		return grid;
	}


	/**
	 * Initially it is 0
	 * Used for id as well
	 * @return the agentCount
	 */
	public int getAgentCount() {
		return agentCount;
	}


	/**
	 * Holds active agents that are on the grid
	 * @return the agentList
	 */
	public ArrayList<Agent> getAgentList() {
		return agentList;
	}

}
