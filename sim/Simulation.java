package sim;

import gui.GUI;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is the main class of this project.
 *
 * @author Kuba Bigaj
 * @version 0.5
 */

@SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
public class Simulation {
    private final int frameDelay;
    ArrayList<Entity> entities = new ArrayList<>();
    private final Boolean[] isPaused = {false};
    private GUI gui;
    private final Scribe scribe;
    Feeder feeder;

    /**
     * Constructor specifying the frame rate.
     *
     * @param frameRate Desired frame rate in fps
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
     * @param args [To be implemented]
     */
    public static void main(String[] args) {
        Scribe scr = new Scribe();
        Simulation s = scr.simInit(args[0]);
        long start, stop;
        try {
            while (true) {
                if (s.isPaused[0]) {
                    s.gui.pause();
                }
                start = System.currentTimeMillis();


                s.feeder.feed(s);
                for (int i = 0; i < s.entities.size(); ++i) {
                    s.entities.get(i).move(s);
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
        this.gui = new GUI(this.entities, imgSize, isFullscreen, this.isPaused, this.scribe);
    }

    void add(Entity e) {
        this.entities.add(e);
        this.gui.add(e);
    }

    void remove(Entity e) {
        this.entities.remove(e);
        this.gui.remove(e);
    }

    static class Feeder {
        Double xPos;
        Double yPos;
        Double range;
        Integer amount;

        public Feeder(Double xPos, Double yPos, Double range, Integer amount) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.range = range;
            this.amount = amount;
        }

        void feed(Simulation s) {
            Random rand = new Random();
            Double x = xPos + rand.nextDouble() * range;
            Double y = yPos + rand.nextDouble() * range;
            s.add(new Food(x, y, rand.nextDouble() % amount, 0.0));
        }

    }

}
