package sim;


import java.util.Random;

import static java.lang.Math.*;
/**
 * This class is responsible for simulating the behavior of Predator objects.
 *
 * @author Paweł Cyganiuk
 */
public class Predator extends Entity {

    private final Integer fovPrey;
    private final Double desiredSeparation;
    /**
     * constructor Predator extends Entity
     *
     * @param posX Value of X coordinate
     * @param posY Value of Y coordinate
     * @param mass Value of mass that impacts breeding and dying
     * @param massDecay value of mass lost per iteration
     * @param dir dirction of movement in radians
     * @param turnRate maximum possible change of direction
     * @param fovPrey range of sight for Prey objects
     * @param desiredSeparation minimum range between Prey objects that they want to keep
     * @param sim simulation
     */
    Predator(Double posX, Double posY, Double mass, Double massDecay,Double dir,Double turnRate,Integer fovPrey,Double desiredSeparation, Simulation sim){
        super(posX,posY,mass,massDecay, sim);
        this.vel=5.0;
        this.dir=toRadians(dir);
        this.turnRate=turnRate;
        this.fovPrey=fovPrey;
        this.desiredSeparation=desiredSeparation;
    }
    /**
     * Method Cohesion is used to steer entities towards average location of the group
     */
    private double cohesion() {
        int i, total = 0;
        double a, b, d, avgDir = 0.0;
        Vector avgPos = new Vector(0, 0);
        for (i = 0; i < this.sim.entities.size(); i++) {
            d = Math.sqrt(((this.posX - this.sim.entities.get(i).posX) * (this.posX - this.sim.entities.get(i).posX)) + ((this.posY - this.sim.entities.get(i).posY) * (this.posY - this.sim.entities.get(i).posY)));
            if (this.sim.entities.get(i) != this && d < fovPrey && this.sim.entities.get(i).getClass().getName().equals("sim.Prey")) {
                avgPos.x += this.sim.entities.get(i).posX;
                avgPos.y += this.sim.entities.get(i).posY;
                total++;
            }
        }
        if (total > 0) {
            avgPos.x /= total;
            avgPos.y /= total;
            a = avgPos.x - this.posX;
            b = avgPos.y - this.posY;
            if (a > 0)
                avgDir = atan(b/a);
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
    /**
     * Method Separation is used to steer entities to the opposite direction than the average location of the group to avoid crowding
     */
    private double separation() {
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
    /**
     * Method Eat is used to navigate objects towards Prey objects
     */
    private double eat(){

        int i,id=-1;
        double d,minD=Double.MAX_VALUE,a,b,avgDir=0.0;
        Vector avgPos=new Vector(0,0);

        if(this.mass<3){
        for(i=0;i<sim.entities.size();i++) {
            d = Math.sqrt(Math.pow(this.posX - sim.entities.get(i).posX, 2) + Math.pow(this.posY - sim.entities.get(i).posY, 2));
            if (sim.entities.get(i) != this && d < fovPrey  && sim.entities.get(i).getClass().getName().equals("sim.Prey")) {
                if (d < minD) {
                    minD = d;
                    id = i;
                }
            }
        }
        if(id>=0) {
            if (Math.abs(this.posX - sim.entities.get(id).posX) < 1 && Math.abs(this.posY - sim.entities.get(id).posY) < 1) {
                this.mass += sim.entities.get(id).mass;
                this.sim.entities.get(id).die();
                System.out.println("Prey slaughtered!");
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
        }}
        return avgDir;

    }
    /**
     * Method Breed is used to add new objects based on their current mass
     */
    private void breed(){
        Random rand = new Random();
        if (this.mass >= 4) {
            this.sim.add(new Predator(this.posX, this.posY, 1.0, this.massDecay, rand.nextDouble() *Math.PI*2, Math.PI / 4,this.fovPrey,this.desiredSeparation,this.sim));
            this.mass -= 1;
        }
    }
    /**
     * Method Move is used to add all movements of the object and turn it into one vector
     */
    @Override
    public void move(){
        double coh=cohesion(),sep=separation(),avgDir,hunger=1+(5/mass),et=eat();
        int div=0;
        if(sep!=0)
            div++;
        if(coh!=0)
            div++;
        if(et!=0)
            div+=hunger;
        if(div>0) {
            if(et!=0)
                avgDir = et;
            else {
                avgDir =sep + coh;
                avgDir/=div;
            }
            dir += avgDir;
        }
        posX+= cos(dir)*vel;
        posY+= sin(dir)*vel;
        breed();
        mass-=massDecay;
        if (this.mass<0){
            this.die();
            System.out.println("Predator starved!");
        }
    }

}
