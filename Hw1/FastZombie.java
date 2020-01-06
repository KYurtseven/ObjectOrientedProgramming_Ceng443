/**
 * FastZombie class inherits from abstract zombie class for code reusing
 *
 */
public class FastZombie extends Zombie{

	/**
	 * Constructor
	 * Initiates abstract zombie class with the following parameters:
	 * ZombieType: Fast
	 * CollisionRange: 2.0
	 * DetectionRange: 20.0
	 * Speed: 20.0
	 * @param name zombie name
	 * @param position zombie's position
	 */
    public FastZombie(String name, Position position) { // DO NOT CHANGE PARAMETERS
        super(name, position, ZombieType.FAST, 2.0, 20.0, 20.0);
    }

    /** 
     * Overrides wandering zombie step in the abstract zombie class
     * Try to find closest soldier and change direction to the soldier and 
     * change state to following
     * If no closest soldier is present, move to the next position
     */
	@Override
	public void wanderingZombieStep(SimulationController controller) {
		Soldier soldier = distanceToClosestSoldier(controller);
		if(soldier != null){
			this.setZombieState(ZombieState.FOLLOWING);
			// Change direction of zombie to the soldier
			this.setDirection(this.getPosition().changeDirection(soldier.getPosition()));
			return;
		}
		this.nextPosition(controller);
	}
	
	/**
	 * Overrides following zombie step in the abstract zombie class
	 * Move to the next position and change state to wandering
	 */
	@Override
	public void followingZombieStep(SimulationController controller) {
		
		this.nextPosition(controller);
		this.setZombieState(ZombieState.WANDERING);
	}

}
