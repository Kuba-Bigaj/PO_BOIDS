package sim;

import gui.GUI;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Simulation {
    private ArrayList<Entity> entities = new ArrayList<>();
    private final int frameDelay;
    private Boolean isPaused = false;

    public Simulation(double frameRate) {
        double delay = frameRate / 60;
        delay *= 1000;
        this.frameDelay = (int) delay;
    }

    public static void main(String args[]) {
        Simulation s = new Simulation(60);
        GUI g = new GUI(s.entities, 600, false, s.isPaused);
        long start, stop;
        try {
            while (true) {
                if (s.isPaused) {
                    g.pause();
                }
                start = System.currentTimeMillis();
                g.update();
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
