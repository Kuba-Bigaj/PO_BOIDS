package sim;
import java.util.ArrayList;

abstract class Entity {
    Double posX,posY,velocity=0.00,direction=0.00,turnRate=0.00,mass,massDecay;

    Entity(Double posX,Double posY,Double mass,Double massDecay)
    {
        this.posX=posX;
        this.posY=posY;
        this.mass=mass;
        this.massDecay=massDecay;
    }
    public void die(ArrayList<Entity> entities)
    {

    }
}
