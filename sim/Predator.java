package sim;

import java.util.ArrayList;
import java.util.Random;

public class Predator extends Entity {
    protected Integer fovPrey;
    Predator(Double posX, Double posY, Double mass, Double massDecay,Double vel,Double dir,Double turnRate,Integer fovPrey,Double desiredSeparation){
        super(posX,posY,mass,massDecay);
        this.vel=vel;
        this.dir=dir;
        this.turnRate=turnRate;
        this.fovPrey=fovPrey;
    }
    Vector Eat(ArrayList<Entity> entities){
        int i,id=-1;
        double d,minD=fovPrey+1.0,a,b,c,cos,sin;
        Vector steer=new Vector(0,0);


        for(i=0;i<entities.size();i++) {
            d = Math.sqrt(Math.pow(this.posX - entities.get(i).posX, 2) + Math.pow(this.posY - entities.get(i).posY, 2));
            if (entities.get(i) != this && d < fovPrey) {
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
    void Move(ArrayList<Entity> entities){
        Vector eating=Eat(entities);
        posX+= eating.x;
        posY+= eating.y;
        Breed(entities);
        mass-=massDecay;
    }
}
