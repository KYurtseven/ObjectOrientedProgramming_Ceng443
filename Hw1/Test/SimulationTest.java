import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test class for simulation runner
 * @author koray
 *
 */
public class SimulationTest {

	private SimulationController controller;
	
	@Before
	public void testSetup() {
		controller = new SimulationController(40,50);
	}
	
	@Test
	public void testMapController() {
		assertEquals(40, controller.getWidth(), 0.0);
		assertEquals(50, controller.getHeight(), 0.0);
	}
	
	// Soldier Constructors
	

	/**
	 * Regular Soldier constructor test
	 */
	@Test
	public void testRegularSoldierConstructor() {
		RegularSoldier regular = new RegularSoldier("Regular1", new Position(20, 30));
		regular.setDirection(new Position(0.6,0.8));
		
		// positions
		assertEquals(20, regular.getPosition().getX(), 0);
		assertEquals(30, regular.getPosition().getY(), 0);
		// directions
		assertEquals(0.6, regular.getDirection().getX(), 0);
		assertEquals(0.8, regular.getDirection().getY(), 0);
		// type
		assertEquals(SoldierType.REGULAR, regular.getSoldierType());
		// state
		assertEquals(SoldierState.SEARCHING, regular.getSoldierState());
		// collision range
		assertEquals(2.0, regular.getCollisionRange(), 0);
		// shooting range
		assertEquals(20.0, regular.getShootingRange(), 0);
		// speed
		assertEquals(5.0, regular.getSpeed(), 0);	
	}
	
	/**
	 * Commando constructor test
	 */
	@Test
	public void testCommandoConstructor() {
		Commando commando = new Commando("Commando1", new Position(10,20));
		commando.setDirection(new Position(1,0));
		
		// positions
		assertEquals(10, commando.getPosition().getX(), 0);
		assertEquals(20, commando.getPosition().getY(), 0);
		// directions
		assertEquals(1, commando.getDirection().getX(), 0);
		assertEquals(0, commando.getDirection().getY(), 0);
		// type
		assertEquals(SoldierType.COMMANDO, commando.getSoldierType());
		// state
		assertEquals(SoldierState.SEARCHING, commando.getSoldierState());
		// collision range
		assertEquals(2.0, commando.getCollisionRange(), 0);
		// shooting range
		assertEquals(10.0, commando.getShootingRange(), 0);
		// speed
		assertEquals(10.0, commando.getSpeed(), 0);	
	}
	
	/**
	 * Sniper constructor test
	 */
	@Test
	public void testSniperConstructor() {
		Sniper sniper = new Sniper("Sniper1", new Position(0, 28));
		sniper.setDirection(new Position(-0.6,0.8));
		
		// positions
		assertEquals(0, sniper.getPosition().getX(), 0);
		assertEquals(28, sniper.getPosition().getY(), 0);
		// directions
		assertEquals(-0.6, sniper.getDirection().getX(), 0);
		assertEquals(0.8, sniper.getDirection().getY(), 0);
		// type
		assertEquals(SoldierType.SNIPER, sniper.getSoldierType());
		// state
		assertEquals(SoldierState.SEARCHING, sniper.getSoldierState());
		// collision range
		assertEquals(5.0, sniper.getCollisionRange(), 0);
		// shooting range
		assertEquals(40.0, sniper.getShootingRange(), 0);
		// speed
		assertEquals(2.0, sniper.getSpeed(), 0);	
	}
	
	// Soldier basic movement cases
	
	/**
	 * move the regular soldier to predefined direction
	 * the next position is inside map
	 */
	@Test
	public void moveRegularSoldierWithoutZombieInsideMap() {
		RegularSoldier regular = new RegularSoldier("Regular1", new Position(20, 30));
		regular.setDirection(new Position(0.6,0.8));
		
		regular.step(controller);
		
		assertEquals(23, regular.getPosition().getX(), 0.0);
		assertEquals(34, regular.getPosition().getY(), 0.0);
		
		assertEquals(SoldierState.SEARCHING, regular.getSoldierState());
		assertEquals(0.6, regular.getDirection().getX(), 0.0);
		assertEquals(0.8, regular.getDirection().getY(), 0.0);
		
	}

	/**
	 * Move the regular soldier to predefined direction
	 * the next position is outside of the map
	 */
	@Test
	public void moveRegularSoldierWithoutZombieOutsideMap() {
		RegularSoldier regular = new RegularSoldier("Regular1", new Position(49, 49));
		regular.setDirection(new Position(0.6,0.8));
		
		// it should be 52,53 but it should not be allowed
		regular.step(controller);
		
		assertThat(52, not(regular.getPosition().getX()));
		assertThat(53, not(regular.getPosition().getY()));
		
		assertEquals(49, regular.getPosition().getX(), 0);
		assertEquals(49, regular.getPosition().getY(), 0);
	}

	/**
	 * move the commando soldier to predefined direction
	 * the next position is inside map
	 */
	@Test
	public void moveCommandoWithoutZombieInsideMap() {
		Commando commando = new Commando("Commando1", new Position(20, 30));
		commando.setDirection(new Position(0.6,0.8));
		
		commando.step(controller);
		
		assertEquals(26, commando.getPosition().getX(), 0.0);
		assertEquals(38, commando.getPosition().getY(), 0.0);
		
		assertEquals(SoldierState.SEARCHING, commando.getSoldierState());
		assertEquals(0.6, commando.getDirection().getX(), 0.0);
		assertEquals(0.8, commando.getDirection().getY(), 0.0);
		
	}

	/**
	 * Move the commando to predefined direction
	 * the next position is outside of the map
	 */
	@Test
	public void moveCommandoWithoutZombieOutsideMap() {
		Commando commando = new Commando("Commando1", new Position(49, 49));
		commando.setDirection(new Position(0.6,0.8));
		
		// it should be 52,53 but it should not be allowed
		commando.step(controller);
		
		assertThat(55, not(commando.getPosition().getX()));
		assertThat(57, not(commando.getPosition().getY()));
		
		assertEquals(49, commando.getPosition().getX(), 0);
		assertEquals(49, commando.getPosition().getY(), 0);
	}

	/**
	 * move the sniper to predefined direction
	 * the next position is inside map
	 */
	@Test
	public void moveSniperWithoutZombieInsideMap() {
		Sniper sniper = new Sniper("Sniper1", new Position(20, 30));
		sniper.setDirection(new Position(0.6,0.8));
		
		sniper.step(controller);
		
		assertEquals(21.2, sniper.getPosition().getX(), 0.01);
		assertEquals(31.6, sniper.getPosition().getY(), 0.01);
		
		// after movement, it is aiming
		assertEquals(SoldierState.AIMING, sniper.getSoldierState());
		assertEquals(0.6, sniper.getDirection().getX(), 0.0);
		assertEquals(0.8, sniper.getDirection().getY(), 0.0);
	}

	/**
	 * move the sniper to predefined direction
	 * the next position is outside of the map
	 */
	@Test
	public void moveSniperWithoutZombieOutsideMap() {
		Sniper sniper = new Sniper("Sniper1", new Position(49, 49));
		sniper.setDirection(new Position(0.6,0.8));
		
		sniper.step(controller);
		
		assertThat(50.2, not(sniper.getPosition().getX()));
		assertThat(50.6, not(sniper.getPosition().getY()));
		
		assertEquals(49, sniper.getPosition().getX(), 0);
		assertEquals(49, sniper.getPosition().getY(), 0);
		
		// after movement, it is aiming
		assertEquals(SoldierState.AIMING, sniper.getSoldierState());
	}
	
	// Zombie constructors
	
	/**
	 * Regular Zombie constructor test
	 */
	@Test
	public void testRegularZombieConstructor() {
		RegularZombie regularZombie = new RegularZombie("RegularZombie1", new Position(10,20));
		regularZombie.setDirection(new Position(-0.8,0.6));
		
		// positions
		assertEquals(10, regularZombie.getPosition().getX(), 0);
		assertEquals(20, regularZombie.getPosition().getY(), 0);
		// directions
		assertEquals(-0.8, regularZombie.getDirection().getX(), 0);
		assertEquals(0.6, regularZombie.getDirection().getY(), 0);
		// type
		assertEquals(ZombieType.REGULAR, regularZombie.getZombieType());
		// state
		assertEquals(ZombieState.WANDERING, regularZombie.getZombieState());
		// collision range
		assertEquals(2.0, regularZombie.getCollisionRange(), 0);
		// shooting range
		assertEquals(20.0, regularZombie.getDetectionRange(), 0);
		// speed
		assertEquals(5.0, regularZombie.getSpeed(), 0);	
	}
	
	/**
	 * Slow Zombie constructor test
	 */
	@Test
	public void testSlowZombieConstructor() {
		SlowZombie slowZombie = new SlowZombie("SlowZombie1", new Position(0,15));
		slowZombie.setDirection(new Position(0.6, 0.8));
		
		// positions
		assertEquals(0, slowZombie.getPosition().getX(), 0);
		assertEquals(15, slowZombie.getPosition().getY(), 0);
		// directions
		assertEquals(0.6, slowZombie.getDirection().getX(), 0);
		assertEquals(0.8, slowZombie.getDirection().getY(), 0);
		// type
		assertEquals(ZombieType.SLOW, slowZombie.getZombieType());
		// state
		assertEquals(ZombieState.WANDERING, slowZombie.getZombieState());
		// collision range
		assertEquals(1.0, slowZombie.getCollisionRange(), 0);
		// shooting range
		assertEquals(40.0, slowZombie.getDetectionRange(), 0);
		// speed
		assertEquals(2.0, slowZombie.getSpeed(), 0);	
	}
	
	/**
	 * Fast Zombie constructor test
	 */
	@Test
	public void testFastZombieConstructor() {
		FastZombie fastZombie = new FastZombie("FastZombie1", new Position(10,10));
		fastZombie.setDirection(new Position(0.6, 0.8));
		
		// positions
		assertEquals(10, fastZombie.getPosition().getX(), 0);
		assertEquals(10, fastZombie.getPosition().getY(), 0);
		// directions
		assertEquals(0.6, fastZombie.getDirection().getX(), 0);
		assertEquals(0.8, fastZombie.getDirection().getY(), 0);
		// type
		assertEquals(ZombieType.FAST, fastZombie.getZombieType());
		// state
		assertEquals(ZombieState.WANDERING, fastZombie.getZombieState());
		// collision range
		assertEquals(2.0, fastZombie.getCollisionRange(), 0);
		// shooting range
		assertEquals(20.0, fastZombie.getDetectionRange(), 0);
		// speed
		assertEquals(20.0, fastZombie.getSpeed(), 0);	
	}
	
	/**
	 * Regular soldier with one regular zombie
	 * In the implementation, the regular zombie will step first
	 * 
	 */
	@Test
	public void regularSoldierWithRegularZombie() {
		System.out.println("BEGIN TEST regularSoldierWithRegularZombie");
		RegularSoldier regularSoldier = new RegularSoldier("RegularSoldier1", new Position(10,5));
		
		// initial Y is 20, because I don't want to kill the soldier at the start
		RegularZombie regularZombie = new RegularZombie("RegularZombie1", new Position(10,20));
		
		// regular soldier will look to the regular zombie
		regularSoldier.setDirection(new Position(0,1));
		// make zombie close to soldier
		regularZombie.setDirection(new Position(0,-1));
		// move regularSoldier and zombie
		controller.addSimulationObject(regularSoldier);
		controller.addSimulationObject(regularZombie);
		
		// Instead of using controller.stepAll()
		// use one step, to avoid while loop in stepAll() method
		regularZombie.step(controller);
		regularSoldier.step(controller);
		
		assertEquals(10, regularSoldier.getPosition().getX(), 0.0);
		assertEquals(10, regularSoldier.getPosition().getY(), 0.0);
		
		assertEquals(10, regularZombie.getPosition().getX(), 0.0);
		assertEquals(15, regularZombie.getPosition().getY(), 0.0);
		
		// after the soldier moves, it should be aiming
		assertEquals(SoldierState.AIMING, regularSoldier.getSoldierState());
		
		// after the zombie moves it should be following
		assertEquals(ZombieState.FOLLOWING, regularZombie.getZombieState());
		

		System.out.println("END TEST regularSoldierWithRegularZombie\n");
	}
	
	@Test
	public void zombieKillsSoldier() {
		System.out.println("BEGIN TEST zombieKillsSoldier");
		RegularSoldier regularSoldier = new RegularSoldier("RegularSoldier1", new Position(10,10));
		
		// initial Y is 20, because I don't want to kill the soldier at the start
		RegularZombie regularZombie = new RegularZombie("RegularZombie1", new Position(10,13));
		
		controller.addSimulationObject(regularSoldier);
		controller.addSimulationObject(regularZombie);
		regularZombie.step(controller);
		
		assertEquals(false, regularSoldier.isActive());
		System.out.println("END TEST zombieKillsSoldier\n");
	}
	
	@Test
	public void longTest1() {
		/*
		System.out.println("BEGIN TEST longTest1");
		RegularSoldier regularSoldier1 = new RegularSoldier("RegularSoldier1", new Position(10,10));
		RegularSoldier regularSoldier2 = new RegularSoldier("RegularSoldier2", new Position(20,10));
		RegularSoldier regularSoldier3 = new RegularSoldier("RegularSoldier3", new Position(30,10));
		
		
		RegularZombie regularZombie1 = new RegularZombie("RegularZombie1", new Position(10,13));
		RegularZombie regularZombie2 = new RegularZombie("RegularZombie2", new Position(10,23));
		RegularZombie regularZombie3 = new RegularZombie("RegularZombie3", new Position(10,33));
		
		controller.addSimulationObject(regularSoldier1);
		controller.addSimulationObject(regularSoldier2);
		controller.addSimulationObject(regularSoldier3);
		
		controller.addSimulationObject(regularZombie1);
		controller.addSimulationObject(regularZombie2);
		controller.addSimulationObject(regularZombie3);
		
		controller.stepAll();
		
		assertEquals(true, controller.isFinished());
		System.out.println("END TEST longTest1\n");
		*/
		
	}
	
	@Test
	public void soldierVsZombie() {

		System.out.println("BEGIN TEST soldierVsZombie");
		RegularSoldier regularSoldier1 = new RegularSoldier("RegularSoldier1", new Position(5,5));
		RegularZombie regularZombie1 = new RegularZombie("RegularZombie1", new Position(35,45));
		
		controller.addSimulationObject(regularSoldier1);
		controller.addSimulationObject(regularZombie1);
		
		controller.stepAll();
		
		assertEquals(true, controller.isFinished());
		

		System.out.println("END TEST soldierVsZombie\n");
	}
	
	/**
	 * Test number of steps that regular zombie has taken
	 * In the 4th step, the regular zombie should be wandering
	 */
	@Test
	public void regularSoldierStepCount() {
		System.out.println("BEGIN TEST regularSoldierStepCount");
		RegularZombie regularZombie1 = new RegularZombie("RegularZombie1", new Position(35,45));
		RegularSoldier regularSoldier1 = new RegularSoldier("RegularSoldier1", new Position(0,0));
		
		// count is 0
		regularZombie1.setZombieState(ZombieState.FOLLOWING);
		
		controller.addSimulationObject(regularSoldier1);
		controller.addSimulationObject(regularZombie1);
		
		// 1
		regularZombie1.step(controller);
		assertEquals(ZombieState.FOLLOWING, regularZombie1.getZombieState());
		
		// 2
		regularZombie1.step(controller);
		assertEquals(ZombieState.FOLLOWING, regularZombie1.getZombieState());
		
		// 3
		regularZombie1.step(controller);
		assertEquals(ZombieState.FOLLOWING, regularZombie1.getZombieState());
		
		// In the 4th step, it should be wandering
		regularZombie1.step(controller);
		assertEquals(ZombieState.WANDERING, regularZombie1.getZombieState());
		
		
		System.out.println("END TEST regularSoldierStepCount\n");
		
	}
	
	@After
	public void testClearController() {
		controller = null;
	}
}
