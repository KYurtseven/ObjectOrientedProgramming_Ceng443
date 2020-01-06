import java.util.Random;

/**
 * Position class for movement in the map
 *
 */
public class Position {
    private double x;
    private double y;
    
    private double length;

    /**
     * Constructor
     * @param x x axis
     * @param y y axis
     * 
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
        
        this.calculateLength();
    }

    /**
     * 
     * @return x axis
     */
    public double getX() {
        return x;
    }

    /**
     * 
     * @return y axis
     */
    public double getY() {
        return y;
    }

    /**
     * Find distance to other position
     * @param other other position
     * @return distance to the other position
     */
    public double distance(Position other) {
        return Math.sqrt(Math.pow(this.x-other.getX(), 2)+Math.pow(this.y-other.getY(), 2));
    }
    
    /**
     * add other position to the current position
     * @param other other position
     */
    public void add(Position other) {
        this.x += other.x;
        this.y += other.y;
        
        this.calculateLength();
    }
    
    /**
     * multiplicate with constant number
     * @param constant constant number
     */
    public void mult(double constant) {
        this.x *= constant;
        this.y *= constant;
        
        this.calculateLength();
    }

    /**
     * Calculates length of the position
     */
    private void calculateLength() {
        this.length = Math.sqrt(Math.pow(x, 2.0)+Math.pow(y, 2.0));
    }
    
    /**
     * Normalizes the position
     * Useful for generating random direction and normalizing
     */
    public void normalize() {
        this.x /= this.length;
        this.y /= this.length;
        
        this.length = 1.0;
    }
    
    /**
     * Generates random direction
     * @param normalize should it be randomized or not
     * @return random direction
     */
    public static Position generateRandomDirection(boolean normalize) {
        Random random = new Random();
        double x = -1+random.nextDouble()*2;
        double y = -1+random.nextDouble()*2;
        
        Position result = new Position(x, y);
        if (normalize)
            result.normalize();
        return result;
    }
    
    /**
     * Changes current object's direction to the target object's position
     * @param targetPosition targetPosition, i.e. for a Soldier it is Zombie position
     * @return new normalized direction of the object
     */
    public Position changeDirection(Position targetPosition) {
    	
    	double x = targetPosition.getX() - this.getX();
    	double y = targetPosition.getY() - this.getY();
    	
    	Position newDirection = new Position(x,y);
    	newDirection.normalize();
    	
    	return newDirection;
    }
    
    /**
     * Overridden copying position
     */
    @Override
    protected Object clone() {
        return new Position(x, y); 
    }

    /**
     * Overridden position equality
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Position other = (Position) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    /**
     * For printing it on the screen
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
    
}
