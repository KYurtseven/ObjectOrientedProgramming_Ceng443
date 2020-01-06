/**
 * RegularZombie class inherits from abstract zombie class for code reusing
 *
 */
public class RegularZombie extends Zombie{

	
	private int stepCount;
	
	/**
	 * Constructor
	 * Initialize the regular zombie with 0 step count
	 * Initiates abstract zombie class with the following parameters:
	 * ZombieType: Regular
	 * CollisionRange: 2.0
	 * DetectionRange: 20.0
	 * Speed: 5.0
	 * @param name zombie name
	 * @param position zombie's position
	 */
    public RegularZombie(String name, Position position) { // DO NOT CHANGE PARAMETERS
        super(name, position, ZombieType.REGULAR, 2.0, 20.0, 5.0);
        
        this.stepCount = 0;
    }
    

    /**
     * Overrides wandering zombie step in the abstract zombie class
     * Go to the next position,
     * Try to find closest soldier and change state to following,
     * If no closest soldier, do nothing
     */
	@Override
	public void wanderingZombieStep(SimulationController controller) {
		this.nextPosition(controller);
		
		Soldier soldier = distanceToClosestSoldier(controller);
		
		if(soldier != null){
			this.setZombieState(ZombieState.FOLLOWING);
		}
	}

	/**
	 * Overrides following zombie step in the abstract zombie class
	 * Move to the next position and 
	 * if the step count of this zombie is 4, change state to wandering
	 */
	@Override
	public void followingZombieStep(SimulationController controller) {
		
		// increment step count
		this.stepCount += 1;
		
		this.nextPosition(controller);
		
		if(this.stepCount == 4) {
			this.setZombieState(ZombieState.WANDERING);
			this.stepCount = 0;
		}
		
		/*
		 * Old code:
		 * I thought that no more than 4 following zombie should be present
		int count = 0;
		for(int i = 0; i < controller.getZombies().size(); i++) {
			
			// if the zombies are active and
			// they are in the following state
			if(controller.getZombies().get(i).isActive()
					&& controller.getZombies().get(i).getZombieState().equals(ZombieState.FOLLOWING)) {
				count++;
				
			}
		}
		if(count == 4) {
			this.setZombieState(ZombieState.WANDERING);
		}
		*/
		
	}
}
