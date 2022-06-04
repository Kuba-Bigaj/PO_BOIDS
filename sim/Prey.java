package sim;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

/**
 * This class is responsible for simulating the behavior of Prey objects.
 *
 * @author Paweł Cyganiuk
 */
public class Prey extends Entity{
    protected Integer fovAlly;
    protected Integer fovEnemy;
    protected Integer fovFood;
    protected Double desiredSeparation;
    /**
     * constructor Prey extends Entity
     */
    Prey(Double posX, Double posY, Double mass, Double massDecay, Double dir, Double turnRate, Integer fovAlly, Integer fovEnemy, Integer fovFood, Double desiredSeparation) {
        super(posX, posY, mass, massDecay);
        this.vel = 1.0;
        this.dir = toRadians(dir);
        this.turnRate = turnRate;
        this.fovAlly = fovAlly;
        this.fovEnemy = fovEnemy;
        this.fovFood = fovFood;
        this.desiredSeparation = desiredSeparation;
    }

    /**
     * Method Alignment is used to steer entities towards average direction of the group
     * @param entities is used as navigated object
     */
    double Alignment(ArrayList<Entity> entities){
        double avgDir=0.0,d;
        int i,total=0;

        for(i=0;i<entities.size();i++)
        {
            d=Math.sqrt(((this.posX - entities.get(i).posX)*(this.posX - entities.get(i).posX)) + ((this.posY - entities.get(i).posY)*(this.posY - entities.get(i).posY)));
            if(entities.get(i)!=this && d<fovAlly && entities.get(i).getClass().getName().equals("sim.Prey")){
                avgDir+=entities.get(i).dir;
                total++;
            }
        }
        if(total>0){
            avgDir/=total;
        }
        avgDir-=this.dir;
        if(avgDir>turnRate)
            avgDir=turnRate;
        else if(avgDir<-turnRate)
            avgDir=-turnRate;
        return avgDir;
    }
    /**
     * Method Cohesion is used to steer entities towards average location of the group
     * @param entities is used as navigated object
     *
     */
    double Cohesion(ArrayList<Entity> entities){
        int i,total=0;
        double a,b,d,avgDir=0.0;
        Vector avgPos=new Vector(0,0);
        for(i=0;i<entities.size();i++) {
            d=Math.sqrt(((this.posX - entities.get(i).posX)*(this.posX - entities.get(i).posX)) + ((this.posY - entities.get(i).posY)*(this.posY - entities.get(i).posY)));
            if (entities.get(i) != this && d < fovAlly && entities.get(i).getClass().getName().equals("sim.Prey")) {
                avgPos.x += entities.get(i).posX;
                avgPos.y += entities.get(i).posY;
                total++;
            }
        }
        if(total>0){
            avgPos.x/=total;
            avgPos.y/=total;
            a = avgPos.x - this.posX;
            b = avgPos.y - this.posY;
            if(a>0)
                avgDir=atan(b/a);
            else if(a<0)
                avgDir=atan((b/a))+Math.PI;
            avgDir-=dir;

            if(avgDir>turnRate)
                avgDir=turnRate;
            else if(avgDir<-turnRate)
                avgDir=-turnRate;

        }
        return avgDir;

    }
    /**
     *  Method Separation is used to steer entities to the opposite direction than the average location of the group to avoid crowding
     * @param entities is used as navigated object
     */
    double Separation(ArrayList<Entity> entities){
        Vector avgPos=new Vector(0,0);
        Vector diff=new Vector(0,0);
        int total=0,i;
        double d,a,b,avgDir=0.0;
        for(i=0;i<entities.size();i++) {
            d=Math.sqrt(((this.posX - entities.get(i).posX)*(this.posX - entities.get(i).posX)) + ((this.posY - entities.get(i).posY)*(this.posY - entities.get(i).posY)));
            if (entities.get(i) != this && d < fovAlly && d<desiredSeparation && entities.get(i).getClass().getName().equals("sim.Prey")) {
                diff.x=posX-entities.get(i).posX;
                diff.y=posY-entities.get(i).posY;
                diff.x/=d;
                diff.y/=d;
                avgPos.x+= posX+diff.x;
                avgPos.y+= posY+diff.y;
                total++;
            }
        }
        if(total >0) {
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
                avgDir=-turnRate;

        }


        return avgDir;
    }
    /**
     * Method Eat is used to navigate objects towards food
     * @param entities is used as a navigated object
     */
    double Eat(ArrayList<Entity> entities){
        int i,id=-1;
        double d,minD=300+1.0,a,b,avgDir=0.0;
        Vector avgPos=new Vector(0,0);


        for(i=0;i<entities.size();i++) {

            d = Math.sqrt(Math.pow(this.posX - entities.get(i).posX, 2) + Math.pow(this.posY - entities.get(i).posY, 2));
            if ( d < 200 && entities.get(i).getClass().getName().equals("sim.Food")) {
                if(d<minD){
                    minD=d;
                    id=i;
                }
            }
        }
        if(id>=0) {
            if (abs(this.posX - entities.get(id).posX) < 5 && abs(this.posY - entities.get(id).posY) < 5) {
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
                    avgDir = atan(b / a) + Math.PI;
                avgDir -= dir;

                if (avgDir > turnRate)
                    avgDir = turnRate;
                else if (avgDir < -turnRate)
                    avgDir = -turnRate;
            }
        }
        return avgDir;
    }
    /**
     * Method Breed is used to add new objects based on the amount of food they ate
     */
    void Breed(ArrayList<Entity> entities){
        Random rand= new Random();
        if(this.mass>=2){
            entities.add(new Prey(this.posX + 0.1, this.posY + 0.1, 1.0, 0.1, rand.nextInt() % 360.0, Math.PI / 4, 20, 20, 20, 15.0));
            mass-=1;
        }
    }
    /**
     * Method Run is used to navigate objects to run from the predators
     * @param entities is used as a navigated object
     */
    double Run(ArrayList<Entity> entities){
        int i,total=0;
        double a,b,d,avgDir=0.0;
        Vector diff=new Vector(0,0);
        Vector avgPos=new Vector(0,0);
        for(i=0;i<entities.size();i++) {
            d = Math.sqrt(Math.pow(this.posX - entities.get(i).posX, 2) + Math.pow(this.posY - entities.get(i).posY, 2));
            if (entities.get(i) != this && d < fovEnemy&& entities.get(i).getClass().getName().equals("sim.Predator")) {
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
            if(a>0)
                avgDir=atan(b/a);
            else if(a<0)
                avgDir=atan((b/a))+Math.PI;
            avgDir-=dir;

            if(avgDir>turnRate)
                avgDir=turnRate;
            else if(avgDir<-turnRate)
                avgDir=-turnRate;



        }
        return avgDir;
    }

    /**
     * Method Move is used to add all movements of the object and turn it into one vector
     *
     * @param entities is used as navigated object
     */

    @Override
    public void move(ArrayList<Entity> entities) {
        double avgDir;
        avgDir = Alignment(entities) + Cohesion(entities) + Separation(entities) + Eat(entities) + Run(entities);
        avgDir/=5;
        dir += avgDir;
        posX += cos(dir) * vel;
        posY += sin(dir) * vel;
        Breed(entities);
        mass -= massDecay;
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
