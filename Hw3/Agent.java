public class Agent extends MazeObject{
    
    private final int id;
    private int collectedGold;
    
    public Agent(Position position, int id) {
        super(position, MazeObjectType.AGENT);
        this.id = id;
        this.collectedGold = 0;
    }
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return the collectedGold
	 */
	public int getCollectedGold() {
		return collectedGold;
	}

	/**
	 * Increase collected gold count
	 */
	public void increaseCollectedGold() {
		this.collectedGold++;
	}
	
}
