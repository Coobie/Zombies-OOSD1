package zombiestarter;

import java.util.ArrayList;
import world.*;
import java.util.List;

/**
 *
 * @author Group 42
 */
public class World {

    private final String info;
    private String inventoryHTML;

    private final String startHTML;
    private Room currentRoom;

    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Item> inventory = new ArrayList<>();

    public World(String info,String inventoryHTML,String startHTML){
        this.info = info;
        this.inventoryHTML = inventoryHTML;
        this.startHTML = startHTML;
    }

    public String getInfo() {
        return info;
    }

    public String getStartHTML(){
        return startHTML;
    }

    public String getInventoryHTML(){
        return inventoryHTML;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public String showInventory(){
        String result = "";
        for (Item inventoryItem: inventory){
            result = result + inventoryItem.getHTML();
        }
        if (result != ""){ //inventory has items
            return result;
        }
        return "Empty"; //The inventory is empty
    }

    public String pickUp(String item){
        //remove from room then add to inventory
        Item i = currentRoom.remove(item);

        if (i != null){ //Item is valid, informs the user that it has been added
            inventory.add(i);
            return ("<b>" + i.getName() + "</b>" + " has been added to inventory");
        }
        //Item has not been found
        return "<b>" + item + "</b>" + " has not been found in " + currentRoom.getName();
    }

    public String drop(String item){
        //loops over items in the inventory until item names matches or there are no more items
        for (Item inventoryItem: inventory){
            if (inventoryItem.getName().equals(item)){
                inventory.remove(inventoryItem);
                currentRoom.add(inventoryItem);
                return ("<b>" + inventoryItem.getName()+ "</b>" + " has been remove from player's inventory");
            }
        }
        return "<b>" + item + "</b>" + " has not been found in inventory";
    }

    public String move(String dir) {
        //Moves the current room
        Room r = currentRoom.move(dir, false);

        if (r != null) { //Entrance has been found
            currentRoom = r;
            return "moved to " + currentRoom.getName();
        }
        else{
            //Entrance could be locked
            r = currentRoom.move(dir, true);

            if (r != null){
                for (Item inventoryItem: inventory){
                    if (inventoryItem.getName().equals("KEY")){
                        inventory.remove(inventoryItem);
                        currentRoom.setEntranceUnlocked(dir);
                        currentRoom = r;
                        return "moved to " + currentRoom.getName();
                    }
                }

                return "<b>You need a key to go through this door!</b>";
            }
        }
        return "Direction is not vaild";
    }

    public boolean getZombies(){ //Check to see whether the room has zombies - required for starting timer on moving rooms
        return currentRoom.getZombieCount() > 0;
    }

    public String killZombies(){
        String output = "You don't have a weapon!";
        if (currentRoom.getZombieCount() != 0){
            for (Item inventoryItem: inventory){ //Checks if the player has either item to kill zombies
                if (inventoryItem.getName().equals("DAISY")){
                    currentRoom.setZombieCount(currentRoom.getZombieCount() - 1);
                    inventory.remove(inventoryItem);    //Removing the Daisy from the players inventory.
                    output = "<b>Zombie slain!</b>";
                    return output;  //Returns that a zombie has been killed in the current room, using the Daisy item.
                } else if(inventoryItem.getName().equals("CHAINSAW")){
                    currentRoom.setZombieCount(currentRoom.getZombieCount() - 1);
                    inventory.remove(inventoryItem);    //Removing the chainsaw from the players inventory.
                    output = "<b>Zombie slain!</b>";
                    return output; //Returns that a zombie has been killed in the current room, using the Chainsaw item.
                }
            }
        } else { //User tries to kill zombies which don't exist (player is paranoid)
            output = "<b>There are no zombies in here to kill!</b>";
        }
        return output;
    }

    public void addEntrances(String name, List<WEntrance> entrances) {
        //Adds the entrances to the rooms
        for (Room room: rooms) {
            if (room.getName().equals(name)) {
                ArrayList<Entrance> roomEntrances = new ArrayList<>();

                for (WEntrance en: entrances) {
                    for (Room r : rooms) {
                        if (r.getName().equals(en.getTo())) {
                            roomEntrances.add(new Entrance(en.getDirection(), en.isLocked(), r));
                        }
                    }
                }
                room.setEntrances(roomEntrances);
            }
        }
    }

    public void addRoom(String name, String description, List<String> itemNames, int zombieCount) {
        ArrayList<Item> roomItems = new ArrayList<>();
        //Adds the rooms to the world
        for (String itemName : itemNames) {
            for (Item item : items) {
                if (item.getName().equals(itemName)) {
                    roomItems.add(item);
                }
            }
        }
        rooms.add(new Room(name, description, roomItems,zombieCount));
    }

    String look() { //Calls look under the current room
        return currentRoom.look();
    }

    public void setStart(String start) { //Sets the starting room for the game
        for (Room room : rooms) {
            if (room.getName().equals(start)) {
                currentRoom = room;
                break;
            }
        }
    }
}
