/**
 * Abstract soldier class
 * 
 * @author koray
 *
 */
public abstract class Soldier extends SimulationObject{

	private SoldierState soldierState;
	private SoldierType soldierType;
	private double collisionRange;
	private double shootingRange;

	/**
	 * Creates abstract soldier with given parameters
	 * Sets default state to SEARCHING
	 * @param name name of the soldier
	 * @param position position of the soldier
	 * @param soldierType type of the soldier
	 * @param collisionRange collision range of the soldier
	 * @param shootingRange shooting range of the soldier
	 * @param speed speed of the soldier
	 */
	public Soldier(String name, 
			Position position,
			SoldierType soldierType,
			double collisionRange,
			double shootingRange,
			double speed
			) {
		super(name, position, speed);
		this.collisionRange = collisionRange;
		this.shootingRange = shootingRange;
		this.soldierType = soldierType;
		
		this.setDirection(Position.generateRandomDirection(true));

		// Default soldier type is searching
		this.soldierState = SoldierState.SEARCHING;
	}


	
	/**
	 * To string method for debugging
	 */
	@Override
	public String toString() {
		return "type: " + this.getSoldierType() + "\n" + 
				"name: " + this.getName() + "\n" +
				"collisionRange: " + this.getCollisionRange() + "\n" +
 				"shootingRange: " + this.getShootingRange() + "\n" +
 				"speed: " + this.getSpeed() + "\n"+
 				"state: " + this.getSoldierState() + "\n" +
 				"direction: " + this.getDirection() + "\n" +
 				"position: " + this.getPosition() + "\n";
	}

	/**
	 * Measures distance to the closest zombie
	 * Returns the zombie if the distance is less than soldier's shooting range
	 * @param controller simulation controller
	 * @return closestZombie
	 */
	public Zombie distanceToClosestZombie(SimulationController controller) {
		
		double minDistance = (double) Integer.MAX_VALUE;
		Zombie closestZombie = null;
		for(Zombie zombie: controller.getZombies()) {
			
			if(zombie.isActive()) {
				double newDistance = this.getPosition().distance(zombie.getPosition());
			
				if(newDistance <= minDistance) {
					minDistance = newDistance;
					closestZombie = zombie;
				}	
			}
		}
		if(minDistance <= this.getShootingRange()) {
			return closestZombie;
		}
		return null;
	}
		
	
	/**
	 * @return the soldierState
	 */
	public SoldierState getSoldierState() {
		return soldierState;
	}




	/**
	 * @param soldierState the soldierState to set
	 */
	public void setSoldierState(SoldierState soldierState) {
		System.out.println(this.getName() + " changed state to " + soldierState);
		this.soldierState = soldierState;
	}




	/**
	 * @return the soldierType
	 */
	public SoldierType getSoldierType() {
		return soldierType;
	}




	/**
	 * @param soldierType the soldierType to set
	 */
	public void setSoldierType(SoldierType soldierType) {
		this.soldierType = soldierType;
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
	 * @return the shootingRange
	 */
	public double getShootingRange() {
		return shootingRange;
	}




	/**
	 * @param shootingRange the shootingRange to set
	 */
	public void setShootingRange(double shootingRange) {
		this.shootingRange = shootingRange;
	}
	
	/**
	 * Abstract searching soldier step
	 * Called in step method of the soldier
	 * @param controller simulation controller
	 */
	public abstract void searchingSoldierStep(SimulationController controller);
	
	

	/**
	 * Step calculation for aiming soldier
	 * Since both regular soldier and sniper do the same thing,
	 * This function is moved to the abstract soldier class.
	 * Since the commando does not aim, this function is overridden by an
	 * empty method in the commando
	 * @param controller simulation controller
	 */
	public void aimingSoldierStep(SimulationController controller) {
		Zombie zombie = distanceToClosestZombie(controller);
		
		if(zombie != null) {
			// change soldier direction to the zombie
			this.setDirection(this.getPosition().changeDirection(zombie.getPosition()));
			// change soldier state
			this.setSoldierState(SoldierState.SHOOTING);
		}
		else {
			this.setSoldierState(SoldierState.SEARCHING);
		}
	}
	
	/**
	 * Abstract shooting soldier step
	 * Called in step method of the soldier
	 * @param controller simulation controller
	 */
	public abstract void shootingSoldierStep(SimulationController controller);

}
