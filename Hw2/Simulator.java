import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.DoubleStream;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class Simulator {

	private static List<Smelter> smelters = new ArrayList<>();
	
	private static List<Constructor> constructors = new ArrayList<>();
	
	private static List<Transporter> transporters = new ArrayList<>();
	
	/**
	 * Sleep random
	 * @param interval
	 */
	public static void sleepRandom(int interval) {
		try {
			Random random = new Random(System.currentTimeMillis());
			DoubleStream stream;
			stream = random.doubles(1, interval- interval*0.01, interval + interval*0.02);
			Thread.sleep((long)stream.findFirst().getAsDouble());
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads input from standard input, creates smelter objects.
	 * Add smelter object to smelters list
	 * @param br buffered reader
	 * @throws IOException exception
	 */
	private static void readAndInitSmelters(BufferedReader br) throws IOException {
		// read smelter count
		System.out.println("Ns: ");
		String nsLine = br.readLine();
		int ns = Integer.parseInt(nsLine);
		
		int smelterCount = 1;
		
		for(; smelterCount<= ns; smelterCount++) {
			
			String lines = br.readLine();
			String[] strs = lines.trim().split("\\s+");

			int[] temp = new int[5];
			for (int i = 0; i < strs.length; i++) {
				temp[i] = Integer.parseInt(strs[i]);
			}
			temp[4] = smelterCount;
			
			Smelter smelter = new Smelter(temp);
			smelters.add(smelter);
		}
	}
	
	/**
	 * Reads input from standard input, creates constructor objects.
	 * Add constructor object to constructors list
	 * @param br buffered reader
	 * @throws IOException exception
	 */
	private static void readAndInitConstructors(BufferedReader br) throws IOException {
		// Read constructor count
		System.out.println("Nc: ");
		// read constructor
		String ncLine = br.readLine();
		int nc = Integer.parseInt(ncLine);
		
		int constructorCount = 1;
		
		for(; constructorCount<= nc; constructorCount++) {
			
			String lines = br.readLine();
			String[] strs = lines.trim().split("\\s+");

			int[] temp = new int[4];
			for (int i = 0; i < strs.length; i++) {
				temp[i] = Integer.parseInt(strs[i]);
			}
			temp[3] = constructorCount;
			
			Constructor constructor = new Constructor(temp);
			
			constructors.add(constructor);
		}
	}
	
	/**
	 * Reads input from standard input, creates transporter objects.
	 * Add transporter object to transporters list
	 * @param br buffered reader
	 * @throws IOException exception
	 */
	private static void readAndInitTransporters(BufferedReader br) throws IOException {
		// Read transporter count
		System.out.println("Nt: ");
		// read transporter
		String ntLine = br.readLine();
		int nt = Integer.parseInt(ntLine);
		
		int transporterCount = 1;
		
		for(; transporterCount<= nt; transporterCount++) {
			
			String lines = br.readLine();
			String[] strs = lines.trim().split("\\s+");

			int[] temp = new int[4];
			for (int i = 0; i < strs.length; i++) {
				temp[i] = Integer.parseInt(strs[i]);
			}
			temp[3] = transporterCount;
			
			
			// find smelter
			Smelter smelter = smelters.get(temp[1] - 1);
			// find constructor
			Constructor constructor = constructors.get(temp[2] - 1);
			
			Transporter transporter = new Transporter(temp, smelter, constructor);
			
			transporters.add(transporter);
			
		}
	}
	
	
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    
			readAndInitSmelters(br);
			readAndInitConstructors(br);
			readAndInitTransporters(br);
			
			br.close();
			
			HW2Logger.InitWriteOutput();
			
			
			int totalThread = smelters.size() + constructors.size() + transporters.size();
			ExecutorService taskList = Executors.newFixedThreadPool(totalThread);
			for(int i = 0; i < smelters.size(); i++) {
				taskList.execute(smelters.get(i));
			}
			for(int i = 0; i < constructors.size(); i++) {
				taskList.execute(constructors.get(i));
			}
			for(int i = 0; i < transporters.size(); i++) {
				taskList.execute(transporters.get(i));
			}
			taskList.shutdown();
			
			//printLists();
			
			
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints smelter, constructor and transporter lists
	 */
	private static void printLists() {
		for(Smelter smelter: smelters) {
			System.out.println(smelter);
		}
		System.out.println("");
		for(Constructor constructor: constructors) {
			System.out.println(constructor);
		}
		System.out.println("");
		for(Transporter transporter: transporters) {
			System.out.println(transporter);
		}
		System.out.println("");
	}
	
	/**
	 * @return the smelters
	 */
	public List<Smelter> getSmelters() {
		return smelters;
	}

	/**
	 * @param smelters the smelters to set
	 */
	public void setSmelters(List<Smelter> smelters) {
		this.smelters = smelters;
	}

	/**
	 * @return the constructors
	 */
	public List<Constructor> getConstructors() {
		return constructors;
	}

	/**
	 * @param constructors the constructors to set
	 */
	public void setConstructors(List<Constructor> constructors) {
		this.constructors = constructors;
	}

	/**
	 * @return the transporters
	 */
	public List<Transporter> getTransporters() {
		return transporters;
	}

	/**
	 * @param transporters the transporters to set
	 */
	public void setTransporters(List<Transporter> transporters) {
		this.transporters = transporters;
	}
} 
