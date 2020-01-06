import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Constructor implements Runnable{

	private final int constructorID;
	
	private final int interval;
	
	private final int capacity;
	
	private final IngotType ingotType;
	
	// 3 for copper, 2 for iron
	private final int requiredMaterialCount;
	
	private final int timeout = 3000;
	
	private int currentMaterialCount;
	
	private boolean isActive;
	
	private int inputMaterial;
	
	private final ReentrantLock l;
	
	private final Condition ingotsReady;
	private final Condition capacityFull;
	
	
	@Override
	public String toString() {
		String line1 = "Constructor: " + constructorID + "\n";
		String line2 = "Interval: " + interval + "\n";
		String line3 = "Capacity: " + capacity + "\n";
		String line4 = "ingotType: " + ingotType + "\n";
		String line5 = "materialCount: " + requiredMaterialCount + "\n";
		String line6 = "-----------------------------\n";
		return line1 + line2 + line3 + line4 + line5 + line6;
	}
	
	/**
	 * Constructor
	 * @param inputs input of constructor
	 */
	public Constructor(int[] inputs) {
		this.interval = inputs[0];
		this.capacity = inputs[1];
		if(inputs[2] == 0) {
			this.ingotType = IngotType.IRON;
			this.requiredMaterialCount = 2;
		}
		else {
			this.ingotType = IngotType.COPPER;
			this.requiredMaterialCount = 3;
		}
		this.constructorID = inputs[3];
		currentMaterialCount = 0;
		inputMaterial = 0;
		setActive(true);
		
		l = new ReentrantLock();
		ingotsReady = l.newCondition();
		capacityFull = l.newCondition();
	}



	/**
	 * @return the constructorID
	 */
	public int getConstructorID() {
		return constructorID;
	}

	/**
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @return the ingotType
	 */
	public IngotType getIngotType() {
		return ingotType;
	}

	/**
	 * @return the required material count to make a product
	 */
	public int getRequiredMaterialCount() {
		return requiredMaterialCount;
	}


	/**
	 * @return the currentMaterialCount
	 */
	public int getCurrentMaterialCount() {
		return currentMaterialCount;
	}


	/**
	 * @param currentMaterialCount the currentMaterialCount to set
	 */
	public void setCurrentMaterialCount(int currentMaterialCount) {
		this.currentMaterialCount = currentMaterialCount;
	}
	
	public void run() {
		HW2Logger.WriteOutput(0, 0, constructorID, Action.CONSTRUCTOR_CREATED);
		try {
			while(true) {
				
				// break if timeout
				if(waitIngots()) {
					break;
				}
				
				HW2Logger.WriteOutput(0, 0, constructorID, Action.CONSTRUCTOR_STARTED);
				
				Simulator.sleepRandom(interval);
				
				constructorProduced();
				
				HW2Logger.WriteOutput(0, 0, constructorID, Action.CONSTRUCTOR_FINISHED);
				
			}
			constructorStopped();
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void constructorStopped() throws InterruptedException{
		HW2Logger.WriteOutput(0, 0, constructorID, Action.CONSTRUCTOR_STOPPED);
	}
	
	/**
	 * Try to acquire the lock.
	 * If the ingot type is COPPER, decrement current material count by 3
	 * if Iron, decrement by 2.
	 * Since there is space in the capacity, notify transporters that are waiting
	 * for capacityFull.
	 * @throws InterruptedException
	 */
	public void constructorProduced() throws InterruptedException{
		l.lock();
		if(ingotType.equals(IngotType.COPPER)) {
			currentMaterialCount -= 3;
		}
		else {
			currentMaterialCount -= 2;
		}
		// there is an empty space
		// transporter can put ingot here
		// notify transporter
		capacityFull.signalAll();
		l.unlock();
	}
	
	/**
	 * Try to acquire the lock. If the current material count is not applicable
	 * (3 for copper, 2 for iron), wait for condition to be satisfied.
	 * Whenever the transporter puts an item, this condition will be signaled
	 * and if there are enough materials, production will be started.
	 * @throws InterruptedException
	 * @returns true if time out
	 */
	public boolean waitIngots() throws InterruptedException{
		
		boolean timeoutFlag = false;
		l.lock();
		while(currentMaterialCount < requiredMaterialCount) {
			
			// return true, if it does NOT timeout
			if(ingotsReady.await(timeout, TimeUnit.MILLISECONDS)) {
				continue;
			}
			else {
				//System.out.println("CONSTRUCTOR TIMED OUT");
				isActive = false;
				timeoutFlag = true;
				break;
			}
				
		}
		l.unlock();
		return timeoutFlag;
	}
	
	/**
	 * Wait if there is no space in the capacity.
	 * Reserve the empty space in inputMaterial.
	 * Since t1 increments input material, t2 cannot exit the while loop,
	 * because the condition still equals to the capacity.
	 * 
	 * @throws InterruptedException
	 */
	public void waitConstructor() throws InterruptedException{
		l.lock();
		while((currentMaterialCount + inputMaterial) >= capacity) {
			capacityFull.await();
		}
		inputMaterial++;
		l.unlock();
	}
	
	/**
	 * Transport unloads an ingot. Constructor receives it.
	 * Since constructor receives it, currentMaterial count is incremented.
	 * The reserved input is decremented.
	 * @throws InterruptedException
	 * @returns true if constructor is alive, false if inactive
	 */
	public boolean unloaded() throws InterruptedException {
		l.lock();
		currentMaterialCount++;
		inputMaterial--;
		if(isActive) {
			ingotsReady.signalAll();
			l.unlock();
			return true;
		}
		else {
			l.unlock();
			return false;
		}
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
