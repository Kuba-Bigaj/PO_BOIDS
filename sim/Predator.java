package sim;


import java.util.Random;

import static java.lang.Math.*;

public class Predator extends Entity {

    protected Integer fovPrey;
    protected Double desiredSeparation;
    Predator(Double posX, Double posY, Double mass, Double massDecay,Double dir,Double turnRate,Integer fovPrey,Double desiredSeparation, Simulation sim){
        super(posX,posY,mass,massDecay, sim);
        this.vel=5.0;
        this.dir=toRadians(dir);
        this.turnRate=turnRate;
        this.fovPrey=fovPrey;
        this.desiredSeparation=desiredSeparation;
    }
    double separation() {
        Vector avgPos = new Vector(0, 0);
        Vector diff = new Vector(0, 0);
        int total = 0, i;
        double d, a, b, avgDir = 0.0;
        for (i = 0; i < this.sim.entities.size(); i++) {
            d = Math.sqrt(((this.posX - this.sim.entities.get(i).posX) * (this.posX - this.sim.entities.get(i).posX)) + ((this.posY - this.sim.entities.get(i).posY) * (this.posY - this.sim.entities.get(i).posY)));
            if (this.sim.entities.get(i) != this && d < desiredSeparation  && this.sim.entities.get(i).getClass().getName().equals("sim.Predator")) {
                diff.x = posX - this.sim.entities.get(i).posX;
                diff.y = posY - this.sim.entities.get(i).posY;
                diff.x /= d;
                diff.y /= d;
                avgPos.x += posX + diff.x;
                avgPos.y += posY + diff.y;
                total++;
            }
        }
        if (total > 0) {
            avgPos.x /= total;
            avgPos.y /= total;
            a = avgPos.x - posX;
            b = avgPos.y - posY;
            if(a>0)
                avgDir=atan(b/a);
            else if(a<0)
                avgDir=atan((b/a))+Math.PI;
            avgDir-=dir;

            if(avgDir>turnRate)
                avgDir=turnRate;
            else if(avgDir<-turnRate)
                avgDir = -turnRate;

        }


        return avgDir;
    }
    double eat(){
        int i,id=-1;
        double d,minD=fovPrey+1.0,a,b,avgDir=0.0;
        Vector avgPos=new Vector(0,0);


        for(i=0;i<sim.entities.size();i++) {
            d = Math.sqrt(Math.pow(this.posX - sim.entities.get(i).posX, 2) + Math.pow(this.posY - sim.entities.get(i).posY, 2));
            if (sim.entities.get(i) != this && d < fovPrey && d<desiredSeparation && sim.entities.get(i).getClass().getName().equals("sim.Prey")) {
                if(d<minD){
                    minD=d;
                    id=i;
                }
            }
        }
        if(id>=0) {
            if (this.posX - sim.entities.get(id).posX < 1 && this.posY - sim.entities.get(id).posY < 1) {
                this.mass += sim.entities.get(id).mass;
                this.sim.entities.get(id).die();
            }
            else {
                avgPos.x = sim.entities.get(id).posX;
                avgPos.y = sim.entities.get(id).posY;
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
            }
        }
        return avgDir;

    }
    void breed(){
        /*Random rand= new Random(); copy from prey
        if(this.mass>=2){
            entities.add(new Predator(this.posX+0.1,this.posY+0.1,1.0,0.1,rand.nextDouble() % 5,rand.nextDouble() % 360,Math.PI/4,20,20.0));
            mass-=1;
        }*/
        Random rand = new Random();
        if (this.mass >= 4) {
            this.sim.add(new Predator(this.posX, this.posY, 1.0, this.massDecay, rand.nextDouble() *Math.PI*2, Math.PI / 4,this.fovPrey,this.desiredSeparation,this.sim));
            this.mass -= 1;
        }
    }
    void Move(){
        double sep=separation(),avgDir=0;
        int div=1;
        if(sep!=0)
            div++;
        avgDir+=eat()+sep;
        avgDir/=div;
        dir+=avgDir;
        posX+= cos(dir)*vel;
        posY+= sin(dir)*vel;
        breed();
        mass-=massDecay;
    }

}
