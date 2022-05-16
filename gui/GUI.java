package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * @author Kuba Bigaj
 * @version 0.1
 */
public class GUI {
    private ArrayList<GUIable> toDraw;
    private int imageSize;
    private boolean isPaused;
    private boolean isFullscreen;

    @SuppressWarnings("all")
    private class PauseListener implements KeyListener {
        public void keyTyped(KeyEvent k) {

        }

        public void keyPressed(KeyEvent k) {

        }

        public void keyReleased(KeyEvent k) {

        }
    }

}
