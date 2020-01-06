import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Smelter class
 */
public class Smelter implements Runnable{

	private final int smelterID;
	
	private final int interval;
	
	private final int capacity;
	
	private final IngotType ingotType;
	
	private final int maxProduce;
	
	private int producedSoFar;
	
	private int currentItemCount;
	
	private final ReentrantLock l;
	
	private final Condition capacityFull;
	
	private final Condition capacityEmpty;

	private int outputMaterial;
	
	private boolean isActive;
	
	
	/**
	 * Constructor
	 * @param inputs
	 */
	public Smelter(int[] inputs) {
		this.interval = inputs[0];
		this.capacity = inputs[1];
		if(inputs[2] == 0) {
			this.ingotType = IngotType.IRON;
		}
		else {
			this.ingotType = IngotType.COPPER;
		}
		this.maxProduce = inputs[3];
		this.smelterID = inputs[4];
		
		currentItemCount = 0;
		producedSoFar = 0;
		outputMaterial = 0;
		isActive = true;
		
		l = new ReentrantLock();
		capacityFull = l.newCondition();
		capacityEmpty = l.newCondition();
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
	 * @return the smelterType
	 */
	public IngotType getSmelterType() {
		return ingotType;
	}


	/**
	 * 
	 * @return the maxProduce
	 */
	public int getMaxProduce() {
		return maxProduce;
	}



	/**
	 * @return the id
	 */
	public int getSmelterID() {
		return smelterID;
	}


	/**
	 * @return the producedSoFar
	 */
	public int getProducedSoFar() {
		return producedSoFar;
	}

	/**
	 * @param producedSoFar the producedSoFar to set
	 */
	public void setProducedSoFar(int producedSoFar) {
		this.producedSoFar = producedSoFar;
	}

	/**
	 * @return the currentItemCount
	 */
	public int getCurrentItemCount() {
		return currentItemCount;
	}

	/**
	 * @param currentItemCount the currentItemCount to set
	 */
	public void setCurrentItemCount(int currentItemCount) {
		this.currentItemCount = currentItemCount;
	}
	

	
	@Override
	public String toString() {
		String line1 = "Smelter: " + smelterID + "\n";
		String line2 = "Interval: " + interval + "\n";
		String line3 = "Capacity: " + capacity + "\n";
		String line4 = "ingotType: " + ingotType + "\n";
		String line5 = "maxProduce: " + maxProduce + "\n";
		String line6 = "producedSoFar: " + producedSoFar + "\n";
		String line7 = "-----------------------------\n";
		return line1 + line2 + line3 + line4 + line5 + line6 + line7;
	}
	
	/**
	 * thread's run method
	 */
	public void run() {
		HW2Logger.WriteOutput(smelterID, 0, 0, Action.SMELTER_CREATED);
		try {
			
			while(isActive) {
				
				waitCanProduce();
				HW2Logger.WriteOutput(smelterID, 0, 0, Action.SMELTER_STARTED);
				
				Simulator.sleepRandom(interval);
				
				ingotProduced();
				HW2Logger.WriteOutput(smelterID, 0, 0, Action.SMELTER_FINISHED);
				
				Simulator.sleepRandom(interval);
				
			}
			smelterStopped();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void smelterStopped() throws InterruptedException{
		HW2Logger.WriteOutput(smelterID, 0, 0, Action.SMELTER_STOPPED);
	}
	
	/**
	 * Increment current item count, so that transporter can take it
	 * Increment produced so far, so that smelter can quit when the limit is satisfied
	 * Signal empty, so that transporters waiting for this smelter will know
	 * that there is an ingot ready to be transported.
	 * @throws InterruptedException
	 */
	public void ingotProduced() throws InterruptedException{
		l.lock();
		currentItemCount++;
		producedSoFar++;
		
		if(producedSoFar == maxProduce) {
			isActive = false;
		}
		// capacity is not empty anymore
		// transporter can take this ingot
		capacityEmpty.signalAll();
		l.unlock();
	}
	
	/**
	 * wait until there is a space in the storage
	 * if the currentItemcount == to capacity, wait for capacityFull condition
	 * Whenever an item is taken(by transporter), they will decrease
	 * currentItemCount and signal capacityFull condition. Threads that are waiting because of
	 * the capacity full condition will wake up and continue to produce.
	 * @throws InterruptedException
	 */
	public void waitCanProduce() throws InterruptedException {
		l.lock();
		while(currentItemCount == capacity) {
			capacityFull.await();
		}
		l.unlock();
	}
	
	
	/**
	 * Transporter will wait until there is an item in the storage.
	 * Whenever the item is available, it reserves that item
	 * so that other transporters cannot take it(item)
	 * @throws InterruptedException
	 * @return true if successful, false if the smelter is inactive and no ingot
	 */
	public boolean waitNextLoad() throws InterruptedException {
		l.lock();
		// a flag used for checking whether the smelter
		// is inactive and has no item currently to be transported
		boolean flag = false;
		
		while((currentItemCount - outputMaterial) == 0) {
			// break if the smelter is inactive
			// and there is no more ingot to take
			if(!isActive) {
				flag = true;
				break;
			}
			capacityEmpty.await();
		}
		// return false if the smelter is inactive and
		// there is no ingot to take
		// if there is ingot to take, flag is false,
		// so, reserve the material
		if(flag) {
			l.unlock();
			return false;
		}
		// reserve the ingot, so that 
		// other transporters cannot take this
		outputMaterial++;
		l.unlock();
		
		return true;
	}
	
	/**
	 * Transporter will signal smelter that there is an empty slot
	 * @throws InterruptedException
	 */
	public void loaded() throws InterruptedException{
		l.lock();
		currentItemCount--;
		outputMaterial--;
		// tell smelter that
		// capacity is not full anymore
		capacityFull.signalAll();
		l.unlock();
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
