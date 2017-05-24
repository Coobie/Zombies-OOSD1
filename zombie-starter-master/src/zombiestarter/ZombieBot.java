/*
 * Author: Group 42
 * Desc: This file contains the heart of dynamic game play for Zombies.
 *       you need to implement each of the methods, as per the game play,
 *       adding support for processing commands comming from the client.
 */
package zombiestarter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Group 42
 */

/**
 * class that implements the ZombieBot interface and plays the game
 * @author br-gaster, improved by Group 42
 */
public class ZombieBot implements world.ZombieBot {
    private boolean quit = false; //Boolean variable Quit that determines what shouldQuit returns.
    private boolean timer = false; //Boolean variable timer that determines what both enableTimer and disableTimer return.
    private int score = 0;
    private final World world;

    ZombieBot(World world) { //Constructor
        this.world = world;
    }

    /**
     * should game quit
     * @return return true if exit program, otherwise false
     */
    @Override
    public boolean shouldQuit() {
        return quit;
    }

    /**
     * prompt to be displayed to user
     * @return
     */
    @Override
    public String begin() {
        //return world.getStartHTML();      This is how it used to be.
        return world.getInfo(); //Doing this makes it display the info as you begin.
    }

    /**
     * compute current score
     * @return current score
     */
    @Override
    public int currentScore() {
        return score;
    }

    /**
     * should timer be enabled? if should be enabled, then method returns true,
     * and goes back into state of not enable.
     * @return true if enable timer, otherwise false
     */
    @Override
    public boolean enableTimer()  {
        return timer;
    }

    /**
     * should timer be disabled? if should be disabled, then method returns
     * true, and goes back into state of don't disable.
     * @return
     */
    @Override
    public boolean disableTimer() {
        return !timer;
    }

    /**
     * process player commands
     * @param cmd to be processed
     * @return output to be displayed
     */
    @Override
    public List<String> processCmd(String cmd) {
        ArrayList<String> result = new ArrayList<>();
        cmd = cmd.toUpperCase();//Setting the input command to upper case for ease of code use.
        String[] cmds = cmd.split(" "); // split cmd by space

        switch(cmds[0]) {
            case "INFO":
                result.add(world.getInfo());
                break;
            case "LOOK":
                result.add(world.look());
                break;
            case "MOVE":
                try{    //Try/Catch used in order to stop user breaking game by not giving a direction.
                    timer = false;
                    disableTimer();

                    String moved = world.move(cmds[1]);
                    if (!moved.equals("Direction is not vaild")){
                        result.add(moved);

                        if (world.getZombies() == true){ //Checks if there are zombies in the room which moved to.
                            timer = true;
                            enableTimer();
                        }
                    } else { //Outputs error message to the user if the direction is not valid
                        result.add("<b>Please enter a valid direction in which to move!</b>");
                    }
                } catch (java.lang.ArrayIndexOutOfBoundsException e){ //Returns message if no direction is given (missing cmds[1])
                    result.add("<b>Error! When attempting to move, please enter the direction in which you would like to go!</b>");
                }
                break;
            case "PICKUP":
                try{    //Try/Catch used in order to stop the user from trying to pickup nothing.
                    result.add(world.pickUp(cmds[1]));
                    result.add("<b>Your inventory is now:</b><br>");
                    result.add(world.getInventoryHTML()+ " "+ world.showInventory());
                    score++;
                    currentScore();
                } catch (java.lang.ArrayIndexOutOfBoundsException e){ //Returns message if no item is given (missing cmds[1])
                    result.add("<b>Error! When picking up an item, please enter the item name you wish to pickup!</b>");
                }
                break;
            case "KILL":
                result.add(world.killZombies());
                if (world.getZombies() != true){
                    timer = false;
                    enableTimer();  //Disabling the timer countdown when all the zombies in the room are slain.
                }
                break;
            case "DROP":
                try{    //Try/Catch used to stop the user from trying to drop nothing.
                    result.add(world.drop(cmds[1]));
                    result.add("<b>Your inventory is now:</b><br>");
                    result.add(world.getInventoryHTML()+ " "+ world.showInventory());
                } catch (java.lang.ArrayIndexOutOfBoundsException e){ //Returns message if no item is given (missing cmds[1])
                    result.add("<br>" + "<b>Error! When dropping an item, please enter the item name you wish to drop!</b>");
                }
                break;
            case "TIMEREXPIRED":
                timer = false;
                disableTimer(); //Disabling the timer countdown.
                quit = true;
                shouldQuit();   //Quitting the game.
                break;
            case "QUIT":
                quit = true;
                shouldQuit();
                break;
            case "INVENTORY":
                result.add("<b>Your inventory:</b><br>");
                result.add(world.getInventoryHTML()+ " "+ world.showInventory());
                break;
            case "BLANK":
                result.add("I beg your pardon?");
                break;
            case "DANCE":   //All good games have a dance function.
                result.add("You bust some sick dance moves. This raises your spirits, but doesn't actually help in any way.");
                break;
            case "":
                break;
            default:
                result.add("<b>That's not a verb I recognise.</b>");
        }
        return result;
     }
}
