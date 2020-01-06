
import java.io.Serializable;

public class MazeObject implements Serializable{
    private Position position;
    private final MazeObjectType type;

    public MazeObject(Position position, MazeObjectType type) {
        this.position = position;
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public MazeObjectType getType() {
        return type;
    }

    @Override
    public String toString() {
        switch(type) {
            case WALL:
                return "X";
            case AGENT:
                return "A";
            case GOLD: 
                return "G";
            case HOLE:
                return "O";
        }
        return "";
    }
    
    
}
