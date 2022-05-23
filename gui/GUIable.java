package gui;

import java.awt.*;

/**
 * Ensures compatibility with the {@link gui.GUI} class.
 */
public interface GUIable {
    /**
     * This method should draw a shape representing the implementing object and return the Graphics2D object to how it was at the beginning.
     *
     * @param pen Graphics2D object in which to draw
     */
    default void draw(Graphics2D pen) {
        pen.setPaint(Color.BLUE);
        pen.drawRoundRect(0, 0, 5, 5, 3, 3);
    }
}
