package sim;

import gui.GUI;

import java.util.ArrayList;

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

    /**
     * Constructor specyfying the framerate.
     *
     * @param frameRate Desired framerate in fps
     */
    public Simulation(double frameRate) {
        double delay = frameRate / 60;
        delay *= 1000;
        this.frameDelay = (int) delay;
    }

    /**
     * Default constructor
     */
    public Simulation() {
        this(60);
    }

    private void guiInit() {
        this.gui = new GUI(this.entities, 600, false, this.isPaused);
    }

    void add(Entity e) {
        this.entities.add(e);
        this.gui.add(e);
    }

    /**
     * Main method of the project
     *
     * @param args [To be implemented]
     */
    public static void main(String args[]) {
        Simulation s = new Simulation(60);
        long start, stop;
        s.guiInit();
        try {
            while (true) {
                if (s.isPaused[0]) {
                    s.gui.pause();
                }
                start = System.currentTimeMillis();
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

}
