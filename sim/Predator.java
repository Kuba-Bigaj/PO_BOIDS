package sim;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class Predator extends Entity {

    protected Integer fovPrey;
    protected Double desiredSeparatoin;
    Predator(Double posX, Double posY, Double mass, Double massDecay,Double vel,Double dir,Double turnRate,Integer fovPrey,Double desiredSeparation, Simulation sim){
        super(posX,posY,mass,massDecay, sim);
        this.vel=vel;
        this.dir=toRadians(dir);
        this.turnRate=turnRate;
        this.fovPrey=fovPrey;
        this.desiredSeparatoin=desiredSeparation;
    }
    double Eat(ArrayList<Entity> entities){
        int i,id=-1;
        double d,minD=fovPrey+1.0,a,b,avgDir=0.0;
        Vector avgPos=new Vector(0,0);


        for(i=0;i<entities.size();i++) {
            d = Math.sqrt(Math.pow(this.posX - entities.get(i).posX, 2) + Math.pow(this.posY - entities.get(i).posY, 2));
            if (entities.get(i) != this && d < fovPrey && d<desiredSeparatoin && entities.get(i).getClass().getName().equals("sim.Prey")) {
                if(d<minD){
                    minD=d;
                    id=i;
                }
            }
        }
        if(id>=0) {
            if (this.posX - entities.get(id).posX < 1 && this.posY - entities.get(id).posY < 1) {
                this.mass += entities.get(id).mass;
                die(entities,entities.get(id));
            }
            else {
                avgPos.x = entities.get(id).posX;
                avgPos.y = entities.get(id).posY;
                a = avgPos.x - posX;
                b = avgPos.y - posY;
                if (a > 0)
                    avgDir = atan(b / a);
                else if (a < 0)
                    avgDir = atan((b / a)) + Math.PI;
                avgDir -= dir;

                if (avgDir > turnRate)
                    avgDir = turnRate;
                else if (avgDir < -turnRate)
                    avgDir = -turnRate;

                if (this.posX - entities.get(id).posX < 1 && this.posY - entities.get(id).posY < 1) {
                    this.mass += entities.get(id).mass;
                    die(entities, entities.get(id));
                }
            }
        }
        return avgDir;

    }
    void Breed(ArrayList<Entity> entities){
        /*Random rand= new Random(); copy from prey
        if(this.mass>=2){
            entities.add(new Predator(this.posX+0.1,this.posY+0.1,1.0,0.1,rand.nextDouble() % 5,rand.nextDouble() % 360,Math.PI/4,20,20.0));
            mass-=1;
        }*/
    }
    void Move(ArrayList<Entity> entities){
        dir+=Eat(entities);
        posX+= cos(dir)*vel;
        posY+= sin(dir)*vel;
        Breed(entities);
        mass-=massDecay;
    }

}
