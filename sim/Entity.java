package sim;

import gui.GUIable;

import java.awt.*;
/**
 * This abstract class contains Entity constructor
 */
public abstract class Entity implements GUIable {
    protected Double posX, posY, vel = 0.00, dir = 0.00, turnRate = 0.00, mass, massDecay;
    protected Simulation sim;
/**
     *
     * @param posX Value of X coordinate
     * @param posY Value of Y coordinate
     * @param mass Value of mass that impacts breeding and dying
     * @param massDecay
     * * @param sim simulation
     */
    Entity(Double posX, Double posY, Double mass, Double massDecay, Simulation sim) {
        this.posX = posX;
        this.posY = posY;
        this.mass = mass;
        this.massDecay = massDecay;
        this.sim=sim;
    }

    /**
     * Mass getter
     * @return This entity's mass
     */
    public Double getMass() {
        return mass;
    }

    /**
     * Method responsible for effectively killing this entity
     */
    public void die() {
        this.sim.remove(this);
    }
    /**
     * Method responsible for simulating the movement and actions of this entity
     */
    public void move() {

    }

    /**
     * This method is responsible for displaying this entity
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
