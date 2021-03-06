package gui;

import sim.Scribe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * This class is responsible for creating a window, drawing the contents of simulation and pausing it.
 *
 * @author Kuba Bigaj
 * @version 0.5
 */
public class GUI {
    private final int imageSize;
    private final JFrame window = new JFrame();
    private final JLabel image;
    private final Graphics2D pen;
    private final ArrayList<GUIable> toDraw = new ArrayList<>();
    private final Boolean[] isPaused;
    private final Scribe scribe;

    /**
     * Constructor designed to work with the Simulation class.
     *
     * @param imageSize    Size of the image to draw in pixels.
     * @param isFullscreen Whether the image should be fullscreen
     * @param isPaused     Reference to a control variable inside implementing class. Should call the {@link #pause() pause} method upon turning true.
     * @param scr          Scribe object used by the Simulation
     */
    public GUI(int imageSize, boolean isFullscreen, Boolean[] isPaused, Scribe scr) {
        if (isFullscreen) {
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            window.setUndecorated(true);
            this.imageSize= (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        } else {
            this.imageSize = imageSize;
        }
        this.isPaused = isPaused;
        this.scribe = scr;
        BufferedImage i = new BufferedImage(this.imageSize, this.imageSize, BufferedImage.TYPE_INT_RGB);
        this.image = new JLabel(new ImageIcon(i));
        this.pen = i.createGraphics();
        pen.setPaint(Color.RED);
        pen.setBackground(Color.WHITE);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.add(image);
        window.addKeyListener(new PauseListener());
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                scribe.fin();
                window.dispose();
                System.exit(0);
            }
        });
        if (!isFullscreen){
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
    /**
     * Method responsible for removing objects from drawing list.
     *
     * @param object Object to be removed.
     */
    public void remove(GUIable object) {
        this.toDraw.remove(object);
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
