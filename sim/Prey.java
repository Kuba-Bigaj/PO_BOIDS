package sim;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Prey extends Entity{
    protected Integer fovAlly;
    protected Integer fovEnemy;
    protected Integer fovFood;
    Prey(Double posX, Double posY, Double mass, Double massDecay,Double vel,Double dir,Double turnRate,Integer fovAlly,Integer fovEnemy,Integer fovFood){
        super(posX,posY,mass,massDecay);
        this.vel=vel;
        this.dir=dir;
        this.turnRate=turnRate;
        this.fovAlly=fovAlly;
        this.fovEnemy=fovEnemy;
        this.fovFood=fovFood;
    }
    Vector Align(ArrayList<Entity> entities){
        Vector steer=new Vector(0,0);
        double avgDir=0,avgVel=0;
        int total=0,i;
        for(i=0;i<entities.size();i++) {
            double d;
            d = Math.sqrt(Math.pow(this.posX - entities.get(i).posX, 2) + Math.pow(this.posY - entities.get(i).posY, 2));
            if (entities.get(i) != this && d < fovAlly) {
                avgDir+=entities.get(i).dir;
                avgVel+=entities.get(i).vel;
                total++;
            }
        }
        if(total >0){
            avgDir/=total;
            avgVel/=total;

        }
        if(avgDir>turnRate)
            avgDir=turnRate;
        else if(avgDir<-turnRate)
            avgDir=-turnRate;
        steer.x=cos(avgDir)*avgVel;
        steer.y=sin(avgDir)*avgVel;
        this.vel=avgVel;
        this.dir=avgDir;

        return steer;
    }

    void move(ArrayList<Entity> entities) {
        Vector alignment = Align(entities);
        Vector cohesion;
        posX += alignment.x;
        posY += alignment.y;
        posX %= 600;
        posX %= 600;
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
        pen.translate(-this.posX.intValue(), -this.posY.intValue());
    }
}
