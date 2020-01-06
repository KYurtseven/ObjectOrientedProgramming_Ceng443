import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 *
 */
public class SimulationRunner {

    public static void main(String[] args) {
        SimulationController simulation = new SimulationController(50, 50);

        RegularSoldier regularSoldier = new RegularSoldier("Soldier1", new Position(10, 10));
        simulation.addSimulationObject(regularSoldier);
        
        SlowZombie slowZombie = new SlowZombie("Zombie1", new Position(10,15));
        simulation.addSimulationObject(slowZombie);
        
        
        while (!simulation.isFinished()) {
            simulation.stepAll();
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimulationRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
