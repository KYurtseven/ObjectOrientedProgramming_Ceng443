/**
 * Abstract Zombie class
 * @author koray
 *
 */
public abstract class Zombie extends SimulationObject{

	private ZombieState zombieState;
	private ZombieType zombieType;
	private double collisionRange;
	private double detectionRange;
	
	/**
	 * Constructor for abstract zombie class
	 * 
	 * @param name name of zombie
	 * @param position position in the map
	 * @param zombieType WANDERING or FOLLOWING
	 * @param collisionRange collision range for zombie
	 * @param detectionRange detection range for zombie
	 * @param speed speed
	 */
	public Zombie(String name, 
			Position position, 
			ZombieType zombieType,
			double collisionRange,
			double detectionRange,
			double speed
			) {
		super(name, position, speed);
		this.collisionRange = collisionRange;
		this.detectionRange = detectionRange;
		this.zombieType = zombieType;

		this.setDirection(Position.generateRandomDirection(true));
		// default zombie state
		this.zombieState = ZombieState.WANDERING;
	}

	
	/**
	 * To string method for debugging
	 */
	@Override
	public String toString() {
		return "type: " + this.getZombieType() + "\n" + 
				"name: " + this.getName() + "\n" +
				"collisionRange: " + this.getCollisionRange() + "\n" +
 				"detectionRange: " + this.getDetectionRange() + "\n" +
 				"speed: " + this.getSpeed() + "\n";
	}

	
	
	/**
	 * @return the zombieState
	 */
	public ZombieState getZombieState() {
		return zombieState;
	}

	/**
	 * @param zombieState the zombieState to set
	 */
	public void setZombieState(ZombieState zombieState) {
		System.out.println(this.getName() + " changed state to " + zombieState);
		this.zombieState = zombieState;
	}

	/**
	 * @return the zombieType
	 */
	public ZombieType getZombieType() {
		return zombieType;
	}

	/**
	 * @param zombieType the zombieType to set
	 */
	public void setZombieType(ZombieType zombieType) {
		this.zombieType = zombieType;
	}

	/**
	 * @return the collisionRange
	 */
	public double getCollisionRange() {
		return collisionRange;
	}

	/**
	 * @param collisionRange the collisionRange to set
	 */
	public void setCollisionRange(double collisionRange) {
		this.collisionRange = collisionRange;
	}

	/**
	 * @return the detectionRange
	 */
	public double getDetectionRange() {
		return detectionRange;
	}

	/**
	 * @param detectionRange the detectionRange to set
	 */
	public void setDetectionRange(double detectionRange) {
		this.detectionRange = detectionRange;
	}
	
	/**
	 * Abstract wandering zombie step
	 * Called in step method of the zombie
	 * @param controller simulation controller
	 */
	public abstract void wanderingZombieStep(SimulationController controller);
	
	/**
	 * Abstract following zombie step
	 * Called in step method of the zombie
	 * @param controller simulation controller
	 */
	public abstract void followingZombieStep(SimulationController controller);
	
	/**
	 * Zombie step
	 * This method will be called every time a zombie calls step method
	 * Later, the specialized wandering or following zombie step methods will be called
	 * inside this method.
	 * 
	 * Try to find closest soldier. If there is no soldier is present, don't
	 * step the zombie, return
	 * If there is a soldier, measure the collision range. Kill the soldier if
	 * the distance is applicable and return.
	 * If the soldier is out of range for killing, step the zombie according to
	 * its state.
	 * 
	 */
	public void step(SimulationController controller) {
		
		double minDistance = (double) Integer.MAX_VALUE;
		Soldier closestSoldier = null;
		for(Soldier soldier: controller.getSoldiers()) {
			
			if(soldier.isActive()) {
				double newDistance = this.getPosition().distance(soldier.getPosition());
				
				if(newDistance <= minDistance) {
					minDistance = newDistance;
					closestSoldier = soldier;
				}
			}
		}
		if(closestSoldier != null) {
			if(minDistance <= (this.getCollisionRange() + closestSoldier.getCollisionRange())) {
				// kill the soldier
				controller.removeSimulationObject(closestSoldier);
				System.out.println(this.getName() + " killed " + closestSoldier.getName() + ".");
				return;
			}
			
			if(this.getZombieState().equals(ZombieState.WANDERING)) {
				wanderingZombieStep(controller);
			}
			else {
				// following state
				followingZombieStep(controller);
			}
		}
		// else part
		// no soldier is present!
		// don't do anything, simulation will handle this
	}
	
	/**
	 * Finds the closest soldier. If it is in the detection range of the zombie,
	 * returns the closest soldier
	 * @param controller simulation controller
	 * @return closestSoldier closest soldier to the zombie 
	 */
	public Soldier distanceToClosestSoldier(SimulationController controller) {
		
		double minDistance = (double) Integer.MAX_VALUE;
		Soldier closestSoldier = null;
		for(Soldier soldier: controller.getSoldiers()) {
			
			if(soldier.isActive()) {
				double newDistance = this.getPosition().distance(soldier.getPosition());
			
				if(newDistance <= minDistance) {
					minDistance = newDistance;
					closestSoldier = soldier;
				}	
			}
		}
		if(minDistance <= this.getDetectionRange()) {
			return closestSoldier;
		}
		return null;
	}
	
}
