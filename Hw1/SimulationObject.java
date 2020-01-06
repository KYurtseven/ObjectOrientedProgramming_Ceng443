/**
 * Simulation object class
 * Main parent class
 *
 */
public abstract class SimulationObject {
    private final String name;
    private Position position;
    private Position direction;
    private final double speed;
    private boolean active;
    
    /**
     * Constructor
     * @param name name of the simulation object
     * @param position position in the map
     * @param speed speed
     */
    public SimulationObject(String name, Position position, double speed) {
        this.name = name;
        this.position = position;
        this.speed = speed;
        this.direction = null;
        this.active = true;
    }

    /**
     * @return name of simulation object
     */
    public String getName() {
        return name;
    }

    /**
     * @return Current position of the object 
     */
    public Position getPosition() {
        return position;
    }

    /**
     * @param position position to be set
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * 
     * @return Current direction of the object
     */
    public Position getDirection() {
        return direction;
    }

    /**
     * 
     * @param direction direction to be set
     */
    public void setDirection(Position direction) {
        this.direction = direction;
    }

    /**
     * 
     * @return Current speed of the object
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * 
     * @return Current active state of the object
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * 
     * @param active active to be set
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
	 * Calculates the next position of the simulation object(Soldier or zombie)
	 * If the next position is out of bounds, changes direction and the simulation
	 * object does not move.
	 * 
	 * If the next position is not out of bounds, the simulation 
	 * object is moved to the next position
	 * 
	 * @param controller simulation controller
	 */
    
	public void nextPosition(SimulationController controller) {
		// To prevent from setting the direction as a side effect, clone it
		Position directionSpeed = (Position) this.getDirection().clone();
		
		directionSpeed.mult(this.getSpeed());
		Position newPosition = (Position) this.getPosition().clone();
		newPosition.add(directionSpeed);
		
		// if simulation object(soldier or zombie) can move to the next position, move it
		// otherwise, don't move it but change the direction to random value
		if(controller.canMove(newPosition)) {
			System.out.println(this.getName() + " moved to " + newPosition + ".");
			this.setPosition(newPosition);
		}
		else {
			this.setDirection(Position.generateRandomDirection(true));
			System.out.println(this.getName() + " changed direction to " + this.getDirection() + ".");
			
		}
	}
	
	/**
	 * abstract step function
	 * @param controller simulation controller
	 */
    public abstract void step(SimulationController controller);
   
}
