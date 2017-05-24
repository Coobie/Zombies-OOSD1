package zombiestarter;

/**
 *
 * @author Group 42
 */
public class Item {

    private final String itemName;
    private final String itemHTML;

    public Item (String itemName, String itemHTML){ //Constructor
        this.itemName = itemName;
        this.itemHTML = itemHTML;
    }

    public String getName(){
        return itemName;
    }

    public String getHTML(){
        return itemHTML;
    }
}
