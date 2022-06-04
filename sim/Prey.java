package sim;

import java.awt.*;
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
        this.vel = 2.0;
        this.dir = toRadians(dir);
        this.turnRate = turnRate;
        this.fovAlly = fovAlly;
        this.fovEnemy = fovEnemy;
        this.fovFood = fovFood;
        this.desiredSeparation = desiredSeparation;
    }

    /**
     * Method Alignment is used to steer entities towards average direction of the group
     *
     * @param s simulation of which the object is part of
     */
    double Alignment(Simulation s) {
        double avgDir = 0.0, d;
        int i, total = 0;

        for (i = 0; i < s.entities.size(); i++) {
            d = Math.sqrt(((this.posX - s.entities.get(i).posX) * (this.posX - s.entities.get(i).posX)) + ((this.posY - s.entities.get(i).posY) * (this.posY - s.entities.get(i).posY)));
            if (s.entities.get(i) != this && d < fovAlly && s.entities.get(i).getClass().getName().equals("sim.Prey")) {
                avgDir += s.entities.get(i).dir;
                total++;
            }
        }
        if(total>0){
            avgDir/=total;
        }
        avgDir-=this.dir;
        if(avgDir>turnRate)
            avgDir = turnRate;
        else if (avgDir < -turnRate)
            avgDir = -turnRate;
        return avgDir;
    }

    /**
     * Method Cohesion is used to steer entities towards average location of the group
     *
     * @param s simulation of which the object is part of
     */
    double Cohesion(Simulation s) {
        int i, total = 0;
        double a, b, d, avgDir = 0.0;
        Vector avgPos = new Vector(0, 0);
        for (i = 0; i < s.entities.size(); i++) {
            d = Math.sqrt(((this.posX - s.entities.get(i).posX) * (this.posX - s.entities.get(i).posX)) + ((this.posY - s.entities.get(i).posY) * (this.posY - s.entities.get(i).posY)));
            if (s.entities.get(i) != this && d < fovAlly && s.entities.get(i).getClass().getName().equals("sim.Prey")) {
                avgPos.x += s.entities.get(i).posX;
                avgPos.y += s.entities.get(i).posY;
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
     *
     * @param s simulation of which the object is part of
     */
    double Separation(Simulation s) {
        Vector avgPos = new Vector(0, 0);
        Vector diff = new Vector(0, 0);
        int total = 0, i;
        double d, a, b, avgDir = 0.0;
        for (i = 0; i < s.entities.size(); i++) {
            d = Math.sqrt(((this.posX - s.entities.get(i).posX) * (this.posX - s.entities.get(i).posX)) + ((this.posY - s.entities.get(i).posY) * (this.posY - s.entities.get(i).posY)));
            if (s.entities.get(i) != this && d < fovAlly && d < desiredSeparation && s.entities.get(i).getClass().getName().equals("sim.Prey")) {
                diff.x = posX - s.entities.get(i).posX;
                diff.y = posY - s.entities.get(i).posY;
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
     * Method Eat is used to navigate objects towards food
     *
     * @param s simulation of which the object is part of
     */
    double Eat(Simulation s) {
        int i, id = -1;
        double d, minD = 300 + 1.0, a, b, avgDir = 0.0;
        Vector avgPos = new Vector(0, 0);


        for (i = 0; i < s.entities.size(); i++) {

            d = Math.sqrt(Math.pow(this.posX - s.entities.get(i).posX, 2) + Math.pow(this.posY - s.entities.get(i).posY, 2));
            if (d < 200 && s.entities.get(i).getClass().getName().equals("sim.Food")) {
                if (d < minD) {
                    minD = d;
                    id = i;
                }
            }
        }
        if (id >= 0) {
            if (abs(this.posX - s.entities.get(id).posX) < 5 && abs(this.posY - s.entities.get(id).posY) < 5) {
                this.mass += s.entities.get(id).mass;
                s.entities.get(id).die(s);

            } else {
                avgPos.x = s.entities.get(id).posX;
                avgPos.y = s.entities.get(id).posY;
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
    void Breed(Simulation s) {
        Random rand = new Random();
        if (this.mass >= 2) {
            s.add(new Prey(this.posX + 0.1, this.posY + 0.1, 1.0, 0.1, rand.nextInt() % 360.0, Math.PI / 4, 20, 20, 20, 15.0));
            mass -= 1;
        }
    }

    /**
     * Method Run is used to navigate objects to run from the predators
     *
     * @param s simulation of which the object is part of
     */
    double Run(Simulation s) {
        int i, total = 0;
        double a, b, d, avgDir = 0.0;
        Vector diff = new Vector(0, 0);
        Vector avgPos = new Vector(0, 0);
        for (i = 0; i < s.entities.size(); i++) {
            d = Math.sqrt(Math.pow(this.posX - s.entities.get(i).posX, 2) + Math.pow(this.posY - s.entities.get(i).posY, 2));
            if (s.entities.get(i) != this && d < fovEnemy && s.entities.get(i).getClass().getName().equals("sim.Predator")) {
                diff.x = posX - s.entities.get(i).posX;
                diff.y = posY - s.entities.get(i).posY;
                diff.x /= d;
                diff.y /= d;
                avgPos.x += posX + diff.x;
                avgPos.y += posX + diff.y;
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
                avgDir=-turnRate;



        }
        return avgDir;
    }

    /**
     * Method Move is used to add all movements of the object and turn it into one vector
     *
     * @param s simulation of which the object is part of
     */

    @Override
    public void move(Simulation s) {
        double avgDir;
        avgDir = Alignment(s) + Cohesion(s) + Separation(s) + Eat(s) + Run(s);
        avgDir /= 5;
        dir += avgDir;
        posX += cos(dir) * vel;
        posY += sin(dir) * vel;
        Breed(s);
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
