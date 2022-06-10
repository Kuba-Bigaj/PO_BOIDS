package sim;

import gui.GUIable;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity implements GUIable {
    protected Double posX, posY, vel = 0.00, dir = 0.00, turnRate = 0.00, mass, massDecay;
    protected Simulation sim;

    Entity(Double posX, Double posY, Double mass, Double massDecay, Simulation sim) {
        this.posX = posX;
        this.posY = posY;
        this.mass = mass;
        this.massDecay = massDecay;
        this.sim=sim;
    }

    public Double getMass() {
        return mass;
    }

    public void die() {
        this.sim.remove(this);
    }

    /**
     * Outdated method, to be removed!
     * @deprecated
     */
    @Deprecated
    public void die(ArrayList<Entity> entities, Entity e) {
        entities.remove(e);
    }

    public void move() {

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
        pen.setPaint(Color.CYAN);
        pen.drawRoundRect(0, 0, 5, 5, 3, 3);
        pen.translate(-this.posX.intValue(), -this.posY.intValue());
    }
}
