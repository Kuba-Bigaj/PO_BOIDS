package gui;

import sim.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * This class is responsible for creating a window, drawing the contents of simulation and pausing it.
 *
 * @author Kuba Bigaj
 * @version 0.5
 * This class is responsible for creating a window, drawing the contents of simulation and pausing it.
 */
public class GUI {
    private final int imageSize;
    private final JFrame window = new JFrame();
    private final JLabel image;
    private final Graphics2D pen;
    private final ArrayList<GUIable> toDraw = new ArrayList<>();
    private final Boolean[] isPaused; //Scuffed, but it works

    /**
     * Constructor designed to work with the Simulation class.
     *
     * @param toDraw       Initial list of entities to draw. Due to the implementation, changes in the list passed will not be reflected in the GUI class. See: {@link #add(GUIable) add} method.
     * @param imageSize    Size of the image to draw in pixels.
     * @param isFullscreen Whether the image should be fullscreen
     * @param isPaused     Reference to a control variable inside implementing class. Should call the {@link #pause() pause} method upon turning true.
     */
    public GUI(ArrayList<Entity> toDraw, int imageSize, boolean isFullscreen, Boolean[] isPaused) {
        this.toDraw.addAll(toDraw);
        this.imageSize = imageSize;
        this.isPaused = isPaused;
        BufferedImage i = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        this.image = new JLabel(new ImageIcon(i));
        this.pen = i.createGraphics();
        pen.setPaint(Color.RED);
        pen.setBackground(Color.WHITE);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(image);
        window.addKeyListener(new PauseListener());
        if (isFullscreen) {
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setUndecorated(true);
        } else {
            window.pack();
        }
        window.setVisible(true);

    }

    /**
     * Method responsible for temporarily stopping the execution of the simulation.
     */
    public void pause() {
        JOptionPane.showMessageDialog(window, "The simulation has been paused!");
        isPaused[0] = false;
    }

    /**
     * Method responsible for adding additional objects to draw.
     *
     * @param object Object to be added. Make sure it has a draw method implemented!
     */
    public void add(GUIable object) {
        toDraw.add(object);
    }

    /**
     * Clears the canvas, redraws the objects and updates the display.
     */
    public void update() {
        pen.clearRect(0, 0, imageSize, imageSize);
        toDraw.forEach((e) -> e.draw(pen));
        image.repaint();
    }

    private class PauseListener implements KeyListener {
        public void keyTyped(KeyEvent k) {

        }

        public void keyPressed(KeyEvent k) {
            if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
                isPaused[0] = true;
            }
        }

        public void keyReleased(KeyEvent k) {

        }
    }

}
