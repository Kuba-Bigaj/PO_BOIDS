package sim;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

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
        avgDir-=dir;
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
    Vector Cohesion(ArrayList<Entity> entities){
        Vector avgPos=new Vector(0,0);
        Vector steer=new Vector(0,0);
        int total=0,i;
        double d,a,b,c,cos,sin;
        for(i=0;i<entities.size();i++) {
            d = Math.sqrt(Math.pow(this.posX - entities.get(i).posX, 2) + Math.pow(this.posY - entities.get(i).posY, 2));
            if (entities.get(i) != this && d < fovAlly) {
                avgPos.x+=entities.get(i).posX;
                avgPos.y+=entities.get(i).posY;
                total++;
            }
        }
        if(total >0) {
            avgPos.x /= total;
            avgPos.y /= total;
            a = avgPos.x - posX;
            b = avgPos.y - posY;
            c = Math.sqrt(Math.pow(avgPos.x - posX, 2) + Math.pow(avgPos.y - posY, 2));
            cos = a / c;
            sin = b / c;
            steer.x = cos*vel;
            steer.y = sin*vel;
        }


        return steer;
    }
    Vector Separation(ArrayList<Entity> entities){
        Vector avgPos=new Vector(0,0);
        Vector steer=new Vector(0,0);
        Vector diff=new Vector(0,0);
        int total=0,i;
        double d,a,b,c,cos,sin;
        for(i=0;i<entities.size();i++) {
            d = Math.sqrt(Math.pow(this.posX - entities.get(i).posX, 2) + Math.pow(this.posY - entities.get(i).posY, 2));
            if (entities.get(i) != this && d < fovAlly) {
                diff.x=posX-entities.get(i).posX;
                diff.y=posY-entities.get(i).posY;
                diff.x/=d;
                diff.y/=d;
                avgPos.x+= posX+diff.x;
                avgPos.y+= posX+diff.y;
                total++;
            }
        }
        if(total >0) {
            avgPos.x /= total;
            avgPos.y /= total;
            a = avgPos.x - posX;
            b = avgPos.y - posY;
            c = Math.sqrt(Math.pow(avgPos.x - posX, 2) + Math.pow(avgPos.y - posY, 2));
            cos = a / c;
            sin = b / c;
            steer.x = cos*vel;
            steer.y = sin*vel;
        }


        return steer;
    }
    Vector Eat(ArrayList<Entity> entities){
        int i,id=-1;
        double d,minD=fovFood+1.0,a,b,c,cos,sin;
        Vector steer=new Vector(0,0);


        for(i=0;i<entities.size();i++) {
            d = Math.sqrt(Math.pow(this.posX - entities.get(i).posX, 2) + Math.pow(this.posY - entities.get(i).posY, 2));
            if (entities.get(i) != this && d < fovFood && entities.get(i).massDecay == 0) {
                if(d<minD){
                    minD=d;
                    id=i;
                }
            }
        }
        if(id>=0) {

            a = entities.get(id).posX - posX;
            b = entities.get(id).posY - posY;
            c = Math.sqrt(Math.pow(entities.get(id).posX - posX, 2) + Math.pow(entities.get(id).posY - posY, 2));
            cos = a / c;
            sin = b / c;
            steer.x = cos*vel;
            steer.y = sin*vel;

            if (this.posX - entities.get(id).posX < 1 && this.posY - entities.get(id).posY < 1) {
                this.mass += entities.get(id).mass;
                die(entities, entities.get(id));
            }
        }
        return steer;
    }
    void Breed(ArrayList<Entity> entities){
        Random rand= new Random();
        if(this.mass>=2){
            entities.add(new Prey(this.posX+0.1,this.posY+0.1,1.0,0.1,rand.nextDouble() % 5,rand.nextDouble() % 360,Math.PI/4,20,20,20));
        }
    }

    void move(ArrayList<Entity> entities) {
        if(mass<1){
            Vector eating=Eat(entities);
            posX+=eating.x;
            posY+=eating.y;
        }
        else {
            Vector alignment = Align(entities);
            Vector cohesion = Cohesion(entities);
            Vector separation = Separation(entities);
            posX += alignment.x;
            posY += alignment.y;
            posX += cohesion.x;
            posY += cohesion.y;
            posX += separation.x;
            posY += separation.y;
            posX %= 600;
            posX %= 600;
        }
        mass-=massDecay;
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
