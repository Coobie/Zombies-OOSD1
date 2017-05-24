package zombiestarter;

import java.util.List;

/**
 *
 * @author Group 42
 */
public class Room {
    private final String name;
    private final String description;
    private List<Entrance> entrances;
    private List<Item> items;
    private int zombieCount;

    public Room(String name, String description, List<Item> items,int zombieCount){ //Constructor
        this.name = name;
        this.description = description;
        this.items = items;
        this.zombieCount = zombieCount;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public Item remove(String item){ //Loops over the items in the room and tries to match the name of the items in the room with that the user typed
        for (Item roomItem: items){
            if (roomItem.getName().equals(item)){
                items.remove(roomItem);
                return roomItem;
            }
        }
        return null;
    }

    public void add(Item item){ //Adds items to the room, used for drop command
        items.add(item);
    }

    public void setEntrances(List<Entrance> entrances) {
        this.entrances = entrances;
    }

    public int getZombieCount(){
        return zombieCount;
    }

    public void setZombieCount(int zC){
        zombieCount = zC;
    }

    public void setEntranceUnlocked(String direction){ //Required for move to set the entrance to be unlocked
        for (Entrance entrance: entrances){
            if(entrance.getDirection().equals(direction)){
                entrance.setLocked(false);
            }
        }
    }

    public Room move(String direction, boolean locked) {
       //loop over entrances and see if there is one with same direction, if so return the room
        for (Entrance entrance: entrances){
            if(entrance.getDirection().equals(direction)){
                if ((locked == false) && entrance.isLocked() != true){ // on the first loop through it checks for unlocked entrances
                    return entrance.getEntranceTo();
                }
                else if(locked == true){ //Second loop through it doesn't check for locked doors, if this succeeds then the door was locked
                    return entrance.getEntranceTo();
               }
           }
       }
       return null;
   }

    public List<Item> getItems(){
        return items;
    }

    public String look() {
        String result = "<h3>You are in <b>" + name + "</b></h3>"+ "Description: " + description + "<br>"; // Adds name and desc
        result = result + "<b>Items in the room</b><br>";
        for (Item item: items) { //Loops over items and shows them to the user
            result = result + item.getHTML();
        }
        result = result + "<br>";
        // loop over entrance and add the (direction, room name)
        result = result + "<b>Available Entrances and the destination</b>" + "<br>";
        for (Entrance entrance: entrances ){ //Lists entrances to the user and whether they are locked
            result = result +"<li>" + entrance.getDirection() + " ";
            result = result + entrance.getEntranceTo().getName()+ " ";
            if(entrance.isLocked() == true){
                result = result + "Locked" +"<br>";
            }
            else{
                result = result + "Unlocked" +"<br>";
            }
        }
        result = result + "</li><b>Zombies: </b>" + zombieCount; //Ouputs zombie count to the user, although they should have noticed!
        return result;
    }


}
