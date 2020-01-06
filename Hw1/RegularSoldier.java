/**
 * Regular soldier class inherits from abstract soldier class for code reusing
 */
public class RegularSoldier extends Soldier{

	
	/**
	 * Constructor
	 * Initiates abstract soldier class with the following parameters:
	 * SoldierType: Regular
	 * CollisionRange: 2.0
	 * ShootingRange: 20.0
	 * Speed: 5.0
	 * @param name soldier name
	 * @param position soldier's position
	 */
    public RegularSoldier(String name, Position position) { // DO NOT CHANGE PARAMETERS
    	super(name, position, SoldierType.REGULAR, 2.0, 20.0, 5.0);
    }
    
    /**
     * Overrides step method in the simulation object class
     * 
     */
	@Override
	public void step(SimulationController controller) {
		// Since we don't have additional state's, we can check it by if/else
		// It violates OCP, but it is stated that no more state will be added
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
	 * Step calculation for searching soldier.
	 * Move the soldier, try to find closest soldier
	 * If there is a closest soldier, change state to aiming
	 * @param controller simulation controller
	 */
	@Override
	public void searchingSoldierStep(SimulationController controller) {
		
		// move soldier
		this.nextPosition(controller);
		
		// find distance to the closest zombie
		Zombie zombie = distanceToClosestZombie(controller);
		
		// can shoot this zombie
		if(zombie != null) {
			this.setSoldierState(SoldierState.AIMING);
		}
	}
	
		
	/**
	 * Step calculation for shooting soldier
	 * Creates bullet,
	 * If closest zombie, change state to aiming,
	 * If not, pick random direction and change state to searching
	 * @param controller simulation controller
	 */
	@Override
	public void shootingSoldierStep(SimulationController controller) {
		Bullet newBullet = new Bullet(
					"Bullet" + controller.getBulletCount(), 
					(Position) this.getPosition().clone(),
					(Position) this.getDirection().clone(),
					40.0);
		
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
