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
@SuppressWarnings("ALL")
public class Simulation {
    private final int frameDelay;
    private ArrayList<Entity> entities = new ArrayList<>();
    private Boolean[] isPaused = {false};
    private GUI gui;
    private Scribe scribe;
    private Feeder feeder;

    /**
     * Constructor specyfying the framerate.
     *
     * @param frameRate Desired framerate in fps
     */
    public Simulation(double frameRate) {
        double delay = frameRate / 60;
        delay *= 1000;
        this.frameDelay = (int) delay;
        this.scribe = new Scribe();
    }

    /**
     * Default constructor
     */
    public Simulation() {
        this(60);
    }

    /**
     * Main method of the project
     *
     * @param args [To be implemented]
     */
    public static void main(String args[]) {
        Simulation s = new Simulation(3);
        long start, stop;
        s.guiInit();
        s.feeder = new Feeder(50.0, 100.0, 200.0, 1);
        try {
            while (true) {
                if (s.isPaused[0]) {
                    s.gui.pause();
                }
                start = System.currentTimeMillis();
                s.feeder.feed(s);
                System.out.println(s.entities.size());

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

    private void guiInit() {
        this.gui = new GUI(this.entities, 600, false, this.isPaused, this.scribe);
    }

    void add(Entity e) {
        this.entities.add(e);
        this.gui.add(e);
    }

    private static class Feeder {
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
            for (int i = 0; i < amount; i++) {
                Double x = xPos + rand.nextDouble() * range;
                Double y = yPos + rand.nextDouble() * range;
                s.add(new Food(x, y, rand.nextDouble() % 10, 0.0));
            }
        }

    }

}
