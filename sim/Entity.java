package sim;
import gui.GUIable;

import java.awt.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public abstract class Entity implements GUIable {
    protected Double posX, posY, vel = 0.00, dir = 0.00, turnRate = 0.00, mass, massDecay;
    protected RoundRectangle2D.Double body = new RoundRectangle2D.Double(0, 0, 5, 5, 3, 3);
    protected Color col=Color.RED;

    Entity(Double posX, Double posY, Double mass, Double massDecay) {
        this.posX = posX;
        this.posY = posY;
        this.mass = mass;
        this.massDecay = massDecay;
    }

    public void die(ArrayList<Entity> entities,Entity boid) {
        entities.remove(boid);
    }

    /**
     * This method is responsible for displaying this object
     *
     * @param pen A Graphics2D object that is used to draw this entity.
     * @author Kuba Bigaj
     */
    @Override
    public void draw(Graphics2D pen) {
        pen.translate(this.posX.intValue(), this.posY.intValue());
        pen.setPaint(Color.RED);
        pen.drawRoundRect(0, 0, 5, 5, 3, 3);
    }
}
