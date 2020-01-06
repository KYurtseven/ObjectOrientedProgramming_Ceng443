/**
 * SlowZombie class inherits from abstract zombie class for code reusing
 *
 */
public class SlowZombie extends Zombie{

	/**
	 * Constructor
	 * Initiates abstract zombie class with the following parameters:
	 * ZombieType: Slow
	 * CollisionRange: 1.0
	 * DetectionRange: 40.0
	 * Speed: 2.0
	 * @param name zombie name
	 * @param position zombie's position
	 */
    public SlowZombie(String name, Position position) { // DO NOT CHANGE PARAMETERS
    	super(name, position, ZombieType.SLOW, 1.0, 40.0, 2.0);
    }

    /**
     * Overrides wandering zombie step in the abstract zombie class
     * Try to find closest soldier and change state to following
     * If no closest soldier is present, move to the next position
     */
	@Override
	public void wanderingZombieStep(SimulationController controller) {
		Soldier soldier = distanceToClosestSoldier(controller);
		if(soldier != null){
			this.setZombieState(ZombieState.FOLLOWING);
			return;
		}
		this.nextPosition(controller);
		
	}

	/**
	 * Overrides following zombie step in the abstract zombie class
	 * Try to find closest soldier and change direction to the soldier
	 * Go to the next position 
	 * Again, try to measure the distance to the same soldier, if it is
	 * less than detection range, change state to wandering
	 */
	@Override
	public void followingZombieStep(SimulationController controller) {
		
		Soldier soldier = distanceToClosestSoldier(controller);
		
		if(soldier != null) {
			// Change direction of zombie to the soldier
			this.setDirection(this.getPosition().changeDirection(soldier.getPosition()));
		}
		this.nextPosition(controller);
		
		double newDistance = this.getPosition().distance(soldier.getPosition());
		
		if(newDistance <= this.getDetectionRange()) {
			this.setZombieState(ZombieState.WANDERING);
		}
	}
}
