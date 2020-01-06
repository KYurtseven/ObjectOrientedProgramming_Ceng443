import java.rmi.Naming;

public class RMIServer {
    public static void main(String[] args){
    	try {
    		IMazeHub server = new MazeHubImpl();
        	Naming.rebind("mazehub", server);
        	//System.out.println("Server is ready");
    	}
    	catch(Exception e) {
    		System.out.println("Error on server: \n" + e.getStackTrace());
    	}
    }
}
