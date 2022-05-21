package gui;

import sim.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author Kuba Bigaj
 * @version 0.1
 */
public class GUI {
    private final int imageSize;
    private final JFrame window = new JFrame();
    private final JLabel image;
    private final Graphics2D pen;
    @SuppressWarnings("all")
    private ArrayList<GUIable> toDraw = new ArrayList<>();
    private Boolean isPaused;

    public GUI(ArrayList<Entity> toDraw, int imageSize, boolean isFullscreen, Boolean isPaused) {
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

    public void pause() {
        JOptionPane.showMessageDialog(window, "The simulation has been paused!");
        isPaused = false;
    }

    public void add(GUIable object) {
        toDraw.add(object);
    }

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
                isPaused = true;
            }
        }

        public void keyReleased(KeyEvent k) {

        }
    }

}
