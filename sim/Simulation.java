package sim;

import gui.GUI;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the main class of this project.
 *
 * @author Kuba Bigaj
 * @version 1.0
 */

@SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
public class Simulation {
    private final int frameDelay;
    ArrayList<Entity> entities = new ArrayList<>();
    private final Boolean[] isPaused = {false};
    private GUI gui;
    private final Scribe scribe;
    static Timer dataOut= new Timer(true);
    Feeder feeder;

    /**
     * Constructor
     * @param isFullscreen Whether the simulation should be fullscreen
     * @param frameRate Desired frame rate in fps
     * @param imgSize Desired image size. Is ignored if the simulation is fullscreen
     * @param scr {@link sim.Scribe Scribe} object to be used for data output
     */
    public Simulation(boolean isFullscreen, Double frameRate, int imgSize, Scribe scr) {
        double delay = frameRate / 60;
        delay *= 1000;
        this.frameDelay = (int) delay;
        this.scribe = scr;
        this.guiInit(isFullscreen, imgSize);
    }

    /**
     * Main method of the project
     *
     * @param args First String specifies the file with desired configuration, the rest is ignored
     */
    public static void main(String[] args) {
        Scribe scr = new Scribe();
        final Simulation s = scr.simInit(args[0]);
        dataOut.schedule(new TimerTask() {
            @Override
            public void run() {
                s.dumpData();
            }
        }, 1000, 1000);
        s.dumpData();
        long start, stop;
        try {
            while (true) {
                if (s.isPaused[0]) {
                    s.gui.pause();
                }
                start = System.currentTimeMillis();
                s.feeder.feed(s);
                for (int i = 0; i < s.entities.size(); i++) {
                    s.entities.get(i).move();
                }
                s.gui.update();
                stop = System.currentTimeMillis();
                if (stop < start + s.frameDelay) {
                    Thread.sleep(s.frameDelay - stop + start);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guiInit(boolean isFullscreen, int imgSize) {
        this.gui = new GUI(imgSize, isFullscreen, this.isPaused, this.scribe);
    }

    /**
     * Adds a member to the simulation
     * @param e Entity to be added
     */
    void add(Entity e) {
        this.entities.add(e);
        this.gui.add(e);
    }
    /**
     * Removes a member to the simulation
     * @param e Entity to be removed
     */
    void remove(Entity e) {
        this.entities.remove(e);
        this.gui.remove(e);
    }

    /**
     * Method used to collect and write Simulation data to a file
     */
    @SuppressWarnings("all")
    void dumpData(){
        Double totalBiomass=0.0;
        Integer creatureNumber=0;
        for (int i=0; i< this.entities.size(); i++){
            String type=this.entities.get(i).getClass().getName();
            if(type.equals("sim.Prey")){
                totalBiomass+=this.entities.get(i).getMass();
                creatureNumber++;
            }
        }
        this.scribe.write("Number of prey:\t\t "+creatureNumber.toString()+"\t Total prey mass:\t "+ totalBiomass.toString()+"\n");
        totalBiomass=0.0;
        creatureNumber=0;
        for (int i=0; i< this.entities.size(); i++){
            String type=this.entities.get(i).getClass().getName();
            if(type.equals("sim.Predator")){
                totalBiomass+=this.entities.get(i).getMass();
                creatureNumber++;
            }
        }
        this.scribe.write("Number of predators:\t "+creatureNumber.toString()+"\t Total predator mass:\t "+ totalBiomass.toString()+"\n");
    }

    /**
     * Class responsible for creating food for the agents in the virtual environment
     */
    static class Feeder {
        Double xPos;
        Double yPos;
        Double range;
        Integer amount;

        /**
         * Constructor
         * @param xPos X position of the feeder (left edge)
         * @param yPos Y position of the feeder (top edge)
         * @param range Range in which the food is dispensed
         * @param amount Maximum amount od food to be given. Actual amount ranges from 0 to the amount specified here.
         */
        public Feeder(Double xPos, Double yPos, Double range, Integer amount) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.range = range;
            this.amount = amount;
        }

        /**
         * Method used to add food to a given Simulation
         * @param s Simulation to receive food
         */
        void feed(Simulation s) {
            Random rand = new Random();
            Double x = xPos + rand.nextDouble() * range;
            Double y = yPos + rand.nextDouble() * range;
            s.add(new Food(x, y, rand.nextDouble() * amount, 0.0, s));
        }

    }

}
