package zombiestarter;

/**
 *
 * @author Group 42
 */
public class Entrance {
    private final String direction;
    private boolean locked;
    private final Room entranceTo;

    public Entrance(String direction, boolean locked, Room entranceTo){ //Constructor
        this.direction = direction;
        this.locked = locked;
        this.entranceTo = entranceTo;
    }

    public String getDirection(){
        return direction;
    }

    public boolean isLocked(){
        return locked;
    }

    public void setLocked(boolean l){
        locked = l;
    }

    public Room getEntranceTo(){
        return entranceTo;
    }
}
