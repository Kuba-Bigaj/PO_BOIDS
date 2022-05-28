package sim;

import gui.GUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This is the main class of this project.
 *
 * @author Kuba Bigaj
 * @version 0.5
 */
@SuppressWarnings("ALL")
public class Simulation {
    private class Scribe {
        private FileWriter writer;

        Scribe() {
            try {
                this.writer = new FileWriter(new File("data_out/" + LocalDateTime.now().toString() + "boids.txt"));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to initialize a file!");
            }
        }
    }

    private final int frameDelay;
    private ArrayList<Entity> entities = new ArrayList<>();
    private Boolean[] isPaused = {false};
    private GUI gui;
    private Scribe scribe;

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
