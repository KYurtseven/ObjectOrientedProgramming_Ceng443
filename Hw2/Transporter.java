
/**
 * Transporter class
 * @author koray
 *
 */
public class Transporter implements Runnable{

	private final int transporterID;
	
	private final int interval;
	
	
	private final Constructor constructor;
	
	private final Smelter smelter;
	
	
	@Override
	public String toString() {
		String line1 = "Transporter: " + transporterID + "\n";
		String line2 = "Interval: " + interval + "\n";
		String line3 = smelter.toString();
		String line4 = constructor.toString();
		String line5 = "-----------------------------\n";
		return line1 + line2 + line3 + line4 + line5;
	}
	

	
	/**
	 * Constructor
	 * @param inputs
	 */
	public Transporter(int[] inputs, Smelter smelter, Constructor constructor) {
		this.interval = inputs[0];
		this.transporterID = inputs[3];
		this.constructor = constructor;
		this.smelter = smelter;
	}

	/**
	 * @return the transporterID
	 */
	public int getTransporterID() {
		return transporterID;
	}
	
	/**
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}


	/**
	 * @return the constructor
	 */
	public Constructor getConstructor() {
		return constructor;
	}

	/**
	 * @return the smelter
	 */
	public Smelter getSmelter() {
		return smelter;
	}
	
	
	public void run() {
		HW2Logger.WriteOutput(smelter.getSmelterID(), transporterID, constructor.getConstructorID(), Action.TRANSPORTER_CREATED);
	
		try {
			while(true) {
				// wait until smelter have an ingot
				if(!smelter.waitNextLoad()) {
					// smelter is inactive and no ingot to take
					// and the transporter cannot take a material
					// because there is no element to be transported
					break;
				}
				HW2Logger.WriteOutput(smelter.getSmelterID(), 
									transporterID, 
									0,
									Action.TRANSPORTER_TRAVEL);
				
				Simulator.sleepRandom(interval);
				
				HW2Logger.WriteOutput(smelter.getSmelterID(), 
									transporterID, 
									0, 
									Action.TRANSPORTER_TAKE_INGOT);
				
				// signal smelter that there is an empty capacity
				smelter.loaded();
				
				// wait constructor until it has an empty space
				constructor.waitConstructor();
				
				HW2Logger.WriteOutput(0, 
									transporterID, 
									constructor.getConstructorID(),
									Action.TRANSPORTER_TRAVEL);
				
				Simulator.sleepRandom(interval);
				
				if(!constructor.unloaded()) {
					// constructor died because of the timeout.
					// It has ingot, but less than the required amount.
					// It has definitely a capacity to put the item in waitConstructor
					// since the constructor died, break
					//System.out.println("TRANSPORTER WILL DIE");
					break;
				}
				
			}
			HW2Logger.WriteOutput(0, transporterID, 0, Action.TRANSPORTER_STOPPED);
		}
		catch(InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
