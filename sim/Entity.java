package sim;
import gui.GUIable;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity implements GUIable {
    Double posX, posY, velocity = 0.00, direction = 0.00, turnRate = 0.00, mass, massDecay;

    Entity(Double posX, Double posY, Double mass, Double massDecay) {
        this.posX = posX;
        this.posY = posY;
        this.mass = mass;
        this.massDecay = massDecay;
    }

    public void die(ArrayList<Entity> entities) {

    }

    /**
     * @param pen A Graphics2D object that is used to draw this entity.
     * @author Kuba Bigaj
     */
    public void draw(Graphics2D pen) {
        //TODO make it translate properly
        pen.setPaint(Color.RED);
        pen.drawRoundRect(0, 0, 5, 5, 3, 3);
    }
}
