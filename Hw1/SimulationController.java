import java.util.ArrayList;
import java.util.List;

/**
 * Simulation controller class 
 * To step a simulation, I have 4 different arrays,
 * namely soldiers, zombies, activeBullets and nextTurnBullets
 */
public class SimulationController {
    private final double height;
    private final double width;

    private boolean isFinished = false;
    
    private List<Soldier> soldiers = new ArrayList<>();
    private List<Bullet> activeBullets = new ArrayList<>();
    private List<Bullet> nextTurnBullets = new ArrayList<>();
    private List<Zombie> zombies = new ArrayList<>();
    private int bulletCount = 0;
    
    /**
     * Constructor
     * @param width width of the map, x
     * @param height heigth of the map, y
     */
    public SimulationController(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    /**
     * Checks soldiers for their alive states
     * if all of them is dead, return false
     * @return
     */
    private boolean isAnyAliveSoldier() {
    	boolean aliveFlag = false;
    	
    	for(int i = 0; i < soldiers.size(); i++) {
    		if(soldiers.get(i).isActive()) {
    			aliveFlag = true;
    			break;
    		}
    	}
    	return aliveFlag;
    }
    /**
     * Checks zombies for their alive states
     * if all of them is dead, return false
     * @return
     */
    private boolean isAnyAliveZombie() {
    	boolean aliveFlag = false;
    	
    	for(int i = 0; i < zombies.size(); i++) {
    		if(zombies.get(i).isActive()) {
    			aliveFlag = true;
    			break;
    		}
    	}
    	return aliveFlag;
    }
    
    //Make sure to fill these methods for grading.
    /**
     * Loops infinitely until all soldiers or all zombies are dead
     * Loops with zombie, bullet, soldier order.
     * At the end of the loop, check for aliveness of soldiers/zombies
     * Finish if necessary. Clear the current bullet array and add next
     * turn bullets to the simulation
     */
    public void stepAll() {
    
    	while(!isFinished) {
    		
        	for(Zombie zombie: this.zombies) {
        		if(zombie.isActive())
        			zombie.step(this);
        	}
        	for(Bullet bullet: this.activeBullets) {
    			bullet.step(this);
        	}
        	for(Soldier soldier: this.soldiers) {
        		if(soldier.isActive())
        			soldier.step(this);
        	}
        	// end of steps
        	// remove bullets from the simulation
        	// add next bullets to the simulation
        	
        	if(!isAnyAliveSoldier()) {
        		this.setFinished(true);
        	}
        	else if(!isAnyAliveZombie()) {
        		this.setFinished(true);
        	}
        	// clear bullet array
        	this.activeBullets = new ArrayList<>();
        	// add next turn bullets
        	for(int i = 0; i < this.nextTurnBullets.size(); i++) {
        		this.activeBullets.add(this.nextTurnBullets.get(i));
        	}
        	// clear next turn bullets
        	this.nextTurnBullets.clear();
    	}
    }
    
    // TODO
    // Add try catch
    /**
     * Add simulation object to the simulation
     * Since we have only 3 parent class (Zombie, Soldier and Bullet)
     * and only the bullet is not an abstract class, I have followed
     * the algorithm:
     * If the object's name is Bullet, add it to Bullet list
     * If the object's superclass name is Soldier, soldier is abstract class,
     * add it to the soldier list
     * Last case is Zombie, same as Soldier
     * 
     * @param obj simulation object to be added
     */
    public void addSimulationObject(SimulationObject obj) {
    	
        Class<? extends SimulationObject> c1 = obj.getClass();
        
        
        if(c1.getName().equals("Bullet")) {
        	
        	this.bulletCount = this.bulletCount + 1;
        	nextTurnBullets.add((Bullet) obj);
        }
        else {
        	// Zombie or Soldier
        	if(c1.getSuperclass().getName().equals("Soldier"))
        		soldiers.add((Soldier) obj);
        	else
        		zombies.add((Zombie) obj);
        }
    }
    
    /**
     * Makes the simulation object's active false
     * @param obj to be removed object
     */
    public void removeSimulationObject(SimulationObject obj) {
        obj.setActive(false);
    }
    

    /**
     * Check map's borders
     * @param nextPosition next position of the simulation object
     * @return true if next position is inside the map's border
     */
    public Boolean canMove(Position nextPosition) {
    	if(nextPosition.getX()< this.width && nextPosition.getX() >= 0) {
    		if(nextPosition.getY() < this.height && nextPosition.getY() >= 0) {
    			return true;
    		}
    	}
    	return false;
    }
    
	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}


	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}


	/**
	 * @return the isFinished
	 */
	public boolean isFinished() {
		return isFinished;
	}


	/**
	 * @param isFinished the isFinished to set
	 */
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	/**
	 * 
	 * @return list of zombies
	 */
	public List<Zombie> getZombies(){
		return this.zombies;
	}
	
	/**
	 * 
	 * @return list of soldiers
	 */
	public List<Soldier> getSoldiers(){
		return this.soldiers;
	}
	
	/**
	 * 
	 * @return list of active bullets
	 */
	public List<Bullet> getActiveBullets(){
		return this.activeBullets;
	}
	
	/**
	 * 
	 * @return list of next turn's bullets
	 */
	public List<Bullet> getNextTurnBullets(){
		return this.nextTurnBullets;
	}


	/**
	 * @return the bulletCount
	 */
	public int getBulletCount() {
		return bulletCount;
	}


	/**
	 * @param bulletCount the bulletCount to set
	 */
	public void setBulletCount(int bulletCount) {
		this.bulletCount = bulletCount;
	}
	
}
