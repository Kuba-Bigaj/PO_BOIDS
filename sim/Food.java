package sim;

import java.awt.*;
/**
 * This class is responsible for implementing Food object acting as food for {@link sim.Prey Prey} agents
 *
 * @author Paweł Cyganiuk
 */
public class Food extends Entity {
    /**
     *Constructor Food extends Entity
     *
     * @param posX Value of X coordinate
     * @param posY Value of Y coordinate
     * @param mass Value of mass that impacts breeding and dying
     * @param massDecay value of mass lost per iteration
     * @param sim simulation
     */
    Food(Double posX, Double posY, Double mass, Double massDecay, Simulation sim) {
        super(posX, posY, mass, massDecay,sim);
    }

    /**
     * This method is responsible for displaying this object
     *
     * @param pen A Graphics2D object that is used to draw this entity.
     * @author Kuba Bigaj
     */
    public void draw(Graphics2D pen) {
        pen.translate(this.posX.intValue(), this.posY.intValue());
        pen.setPaint(Color.GREEN);
        pen.drawRoundRect(0, 0, 5, 5, 3, 3);
        pen.translate(-this.posX.intValue(), -this.posY.intValue());
    }
}
