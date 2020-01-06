/**
 * Bullet class
 * Inherited from SimulationObject class
 * 
 * @author koray
 *
 */
public class Bullet extends SimulationObject{

	/**
	 * Constructor
	 * @param name bullet name
	 * @param position current position of the soldier
	 * @param direction current direction of the soldier
	 * @param speed speed of the bullet
	 */
	public Bullet(String name, 
			Position position, 
			Position direction,
			double speed
			) {
		super(name, position, speed);
		
		this.setDirection(direction);
	}

	/**
	 * Overridden step function of simulation object class
	 * Divide the path into N number of steps.
	 * At each step advance the bullet.
	 * At each step, find the closest zombie, if there is a collision
	 * make zombie and bullet inactive and break the for loop, exit the function
	 * 
	 * Continue until hitting a zombie or going outside of the map
	 */
	@Override
	public void step(SimulationController controller) {
		int steps = (int) (this.getSpeed() / 1.0);
	
		for(int i = 0; i < steps; i++) {
			
			double minDistance = (double) Integer.MAX_VALUE;
			Zombie closestZombie = null;
			
			// find closest zombie
			for(Zombie zombie: controller.getZombies()) {
				
				if(zombie.isActive()) {

					double newDistance = this.getPosition().distance(zombie.getPosition());
					
					if(newDistance <= minDistance) {
						minDistance = newDistance;
						closestZombie = zombie;
					}
				}		
			}
			if(closestZombie != null) {
				if(minDistance <= closestZombie.getCollisionRange()) {
					controller.removeSimulationObject(closestZombie);
					controller.removeSimulationObject(this);
					System.out.println(this.getName() + " hit " + closestZombie.getName());
					return;
				}
			}
			
			// move bullet to the next position
			Position directionSpeed = (Position) this.getDirection().clone();
			
			directionSpeed.mult(this.getSpeed());
			Position newPosition = (Position) this.getPosition().clone();
			newPosition.add(directionSpeed);
			
			if(controller.canMove(newPosition)) {
				this.setPosition(newPosition);
			}
			else {
				// out of bounds
				System.out.println(this.getName() + " moved out of bounds.");
				return;
			}
		}
		// Steps are over, bullet did not collide with zombie
		// or did not move out of bounds
		// it hit the ground
		System.out.println(this.getName() + " dropped to the ground at " + this.getPosition() +".");
	}

	/**
	 * To string method for debugging
	 */
	@Override
	public String toString() {
		return 
				"name: " + this.getName() + "\n" +
				"speed: " + this.getSpeed() + "\n"+
 				"direction: " + this.getDirection() + "\n" +
 				"position: " + this.getPosition() + "\n";
	}
}
