package zombiestarter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.ZombieServer;
import world.WItem;
import world.WRoom;
import world.WorldLoader;

/**
 *
 * @author Group 42
 */
public class ZombieStarter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // use a try/catch block to handle the case when opening
        // a socket fails...
         try {
            //Implement a WorldLoader Class
            WorldLoader wl = new WorldLoader();

            //Implement a World class. The World class takes in parameters.
            World world = new World(wl.getInfo(), wl.getInventoryHtml(), wl.getStartHtml());

            for (WItem item : wl.getItems()) {
                world.addItem(new Item(item.getName(), item.getHtml())); //Adding items to the world.
            }

            for (WRoom room : wl) {
                world.addRoom(room.getName(), room.getDescription(), room.getItems(),room.getZombieCount()); //Adding rooms to the world.
            }

            for (WRoom room : wl) {
                world.addEntrances(room.getName(), room.getEntrances()); //Adding entrances to the rooms within the world.
            }

            world.setStart(wl.getStart());//Sets the starting room



            // create an instane of our server to commnicate with the
            // web frontend.
            InetAddress ip = InetAddress.getLocalHost();

            // now connect to the server
            ZombieServer zs = new ZombieServer(
                    // get host address, rather than using 127.0.0.1, as this
                    // will then be displayed when server waits for connection
                    // which allows the address to then be typed into client.
                    ip.getHostAddress(),
                    8085,
                    new ZombieBot(world));
         } catch (UnknownHostException ex) {
            Logger.getLogger(
                    ZombieStarter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
