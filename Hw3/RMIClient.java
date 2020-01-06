import java.rmi.Naming;
import java.util.Scanner;



public class RMIClient {
    public static void main(String[] args) {
        
    	try {
    		IMazeHub server = (IMazeHub) Naming.lookup("mazehub");
    		
    		IMaze maze = null;
    		
	        Scanner scanner = new Scanner(System.in);
	        ParsedInput parsedInput = null;
	        String input;
	        while( true ) {
	            input = scanner.nextLine();
	            try {
	                parsedInput = ParsedInput.parse(input);
	            }
	            catch (Exception ex) {
	                parsedInput = null;
	            }
	            if ( parsedInput == null ) {
	                System.out.println("Wrong input format. Try again.");
	                continue;
	            }
	            try{
		            switch(parsedInput.getType()) {
		                case CREATE_MAZE:
		                	int width = (Integer) parsedInput.getArgs()[0];
		                	int height = (Integer) parsedInput.getArgs()[1];
		                	server.createMaze(width, height);
		                    break;
		                case DELETE_MAZE:
		                	int index = (Integer) parsedInput.getArgs()[0];
		                	
		                	boolean response1 = server.removeMaze(index);
		                	printResponse(response1);
		                	
		                	break;
		                case SELECT_MAZE:
		                	int index2 = (Integer) parsedInput.getArgs()[0];
		                	
		                	maze = server.getMaze(index2);
		                    break;
		                    
		                case PRINT_MAZE:
		                	System.out.println(maze.print());
		                    
		                	break;
		                case CREATE_OBJECT:
		                	int x = (Integer) parsedInput.getArgs()[0];
		                	int y = (Integer) parsedInput.getArgs()[1];
		                	Position position = new Position(x, y);
		                	MazeObjectType type = (MazeObjectType) parsedInput.getArgs()[2];
		                	
		                	boolean response2 = maze.createObject(position, type);
		                	printResponse(response2);
		                	
		                	break;
		                case DELETE_OBJECT:
		                	int x2 = (Integer) parsedInput.getArgs()[0];
		                	int y2 = (Integer) parsedInput.getArgs()[1];
		                	Position position2 = new Position(x2, y2);
		                	
		                	boolean response3 = maze.deleteObject(position2);
		                	printResponse(response3);
		                    
		                	break;
		                case LIST_AGENTS:
		                    Agent[] agents = maze.getAgents();
		                    for(int i = 0; i < agents.length; i++) {
		                    	String str = "Agent" + agents[i].getId() + " at "
		                    			+ agents[i].getPosition() + ". Gold collected: "
	                					+ agents[i].getCollectedGold() + ".\n";
		                    			
		                    	System.out.print(str);
		                    }
		                	break;
		                case MOVE_AGENT:
		                	int id3 = (Integer) parsedInput.getArgs()[0];
		                	int x3 = (Integer) parsedInput.getArgs()[1];
		                	int y3 = (Integer) parsedInput.getArgs()[2];
		                	Position position3 = new Position(x3, y3);
		                	
		                	boolean response4 = maze.moveAgent(id3, position3);
		                	printResponse(response4);
		                	
		                    break;
		                case QUIT:
		                	
		                    return;
		            }
	            }
	            catch(Exception e) {
	            	// TODO
	            }

	        }
    	} catch (Exception e) {
			System.out.println("Exception in client: "+ e);
		}
    }
    
    /**
     * Helper method for printing the rmi client output
     * @param response true if successful, false otherwise
     */
    private static void printResponse(boolean response) {
    	if(response) {
    		System.out.println("Operation Success.");
    	}
    	else {
    		System.out.println("Operation Failed.");
    	}
    }
}
