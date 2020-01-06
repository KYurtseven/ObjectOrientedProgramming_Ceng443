/**
 * Sniper class inherits from abstract soldier class for code reusing
 */
public class Sniper extends Soldier{

	/**
	 * Constructor
	 * Initiates abstract soldier class with the following parameters:
	 * SoldierType: Sniper
	 * CollisionRange: 5.0
	 * ShootingRange: 40.0
	 * Speed: 2.0
	 * @param name soldier name
	 * @param position soldier's position
	 */
    public Sniper(String name, Position position) { // DO NOT CHANGE PARAMETERS
    	super(name, position, SoldierType.SNIPER, 5.0, 40.0, 2.0);
    }

    /**
     * Overrides step method in the simulation object class
     * 
     */
	@Override
	public void step(SimulationController controller) {
		if(this.getSoldierState().equals(SoldierState.SEARCHING)) {
			searchingSoldierStep(controller);
		}
		else if(this.getSoldierState().equals(SoldierState.AIMING)) {
			aimingSoldierStep(controller);
		}
		else // Shooting
			shootingSoldierStep(controller);
	}

	/**
	 * Step calculation for searching sniper
	 * Go to the next position
	 * Change state to aiming
	 */
	@Override
	public void searchingSoldierStep(SimulationController controller) {
		
		// move soldier
		this.nextPosition(controller);
		
		this.setSoldierState(SoldierState.AIMING);
	}

	/**
	 * Step calculation for shooting soldier
	 * Creates bullet,
	 * Try to find another zombie while in the shooting state. If found, change
	 * state to aiming.
	 * If no closest zombie is present, change random direction and search
	 * for another zombie, change state to searching
	 */
	@Override
	public void shootingSoldierStep(SimulationController controller) {
		Bullet newBullet = new Bullet(
				"Bullet" + controller.getBulletCount(), 
				(Position) this.getPosition().clone(),
				(Position) this.getDirection().clone(),
				100.0);
		
		System.out.println(this.getName() + " fired " + newBullet.getName() + 
				" to direction " + newBullet.getDirection() + "."
				);
		
		controller.addSimulationObject(newBullet);
		
		Zombie zombie = distanceToClosestZombie(controller);
		
		if(zombie != null) {
			this.setSoldierState(SoldierState.AIMING);
		}
		else {
			// change soldier's direction to random value
			this.setDirection(Position.generateRandomDirection(true));
			
			this.setSoldierState(SoldierState.SEARCHING);
		}
		
	}

}
