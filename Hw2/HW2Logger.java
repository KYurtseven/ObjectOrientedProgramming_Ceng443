

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Akpak
 */


public class HW2Logger {
    private static long startTime;
    
    private static long getTimeDifference() {
        return System.nanoTime()-startTime;
    }
    public static void InitWriteOutput() {
        startTime = System.nanoTime();
    }
    
    public static synchronized void WriteOutput(int smelterID, int transporterID, int constructorID, Action action) {
        long currentThreadID = Thread.currentThread().getId();
        long timestamp = getTimeDifference();
        
        System.out.printf("ThreadID: %d, Smelter: %d, Transporter: %d, Constructor: %d, Action: %s, TimeStamp: %f ms.\n", currentThreadID, smelterID, transporterID, constructorID, action.toString(), timestamp/1000000.0);
    }
    
}
