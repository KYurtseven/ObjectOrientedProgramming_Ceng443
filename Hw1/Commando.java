/**
 * Commando class inherits from abstract soldier class for code reusing
 */
public class Commando extends Soldier{

	/**
	 * Constructor
	 * Initiates abstract soldier class with the following parameters:
	 * SoldierType: Commando
	 * CollisionRange: 2.0
	 * ShootingRange: 10.0
	 * Speed: 10.0
	 * @param name soldier name
	 * @param position soldier's position
	 */
    public Commando(String name, Position position) { // DO NOT CHANGE PARAMETERS
    	super(name, position, SoldierType.COMMANDO, 2.0, 10.0, 10.0);
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
		else if(this.getSoldierState().equals(SoldierState.SHOOTING)) {
			shootingSoldierStep(controller);
		}
		else {
			// Commando is aiming, ERROR
			System.out.println("Commando is aiming?????????????? \n\n\n");
		}
	}

	/**
	 * Step calculation for searching commando
	 * Find closest zombie and tries to shoot it
	 * If no closest zombie is present, moves to next position and
	 * again tries to find a closest zombie and shoot it
	 */
	@Override
	public void searchingSoldierStep(SimulationController controller) {
		// find distance to the closest zombie
		Zombie zombie = distanceToClosestZombie(controller);
		
		if(zombie != null) {
			this.setSoldierState(SoldierState.SHOOTING);
		}
		else {
			// move soldier
			this.nextPosition(controller);
			
			// find distance to the closest zombie
			Zombie zombie2 = distanceToClosestZombie(controller);
			
			// can shoot this zombie
			if(zombie2 != null) {
				// TODO
				// change soldier direction to the zombie
				this.setDirection(this.getPosition().changeDirection(zombie2.getPosition()));
				// change soldier state
				this.setSoldierState(SoldierState.SHOOTING);
			}
		}
	}

	/**
	 * Empty overwritten method
	 * Commando does not aim!
	 */
	@Override
	public void aimingSoldierStep(SimulationController controller) {
		// It does nothing
	}

	/**
	 * Step calculation for shooting commando
	 * Creates bullet,
	 * Try to find another zombie while in the shooting state
	 * If no closest zombie is present, change random direction and search
	 * for another zombie
	 */
	@Override
	public void shootingSoldierStep(SimulationController controller) {
		Bullet newBullet = new Bullet(
				"Bullet" + controller.getBulletCount(), 
				(Position) this.getPosition().clone(),
				(Position) this.getDirection().clone(),
				40.0);
		
		System.out.println(this.getName() + " fired " + newBullet.getName() + 
				" to direction " + newBullet.getDirection() + ".");
		
		controller.addSimulationObject(newBullet);
		
		// find distance to the closest zombie
		Zombie zombie = distanceToClosestZombie(controller);
		
		if(zombie != null) {
			// change soldier direction to the zombie
			this.setDirection(this.getPosition().changeDirection(zombie.getPosition()));
		}
		else {
			this.setDirection(Position.generateRandomDirection(true));
			this.setSoldierState(SoldierState.SEARCHING);
		}
	}

}
