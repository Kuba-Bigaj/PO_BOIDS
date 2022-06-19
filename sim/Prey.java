package sim;

import java.awt.*;
import java.util.Random;

import static java.lang.Math.*;

/**
 * This class is responsible for simulating the behavior of Prey objects.
 *
 * @author Paweł Cyganiuk
 */
public class Prey extends Entity {
	private final Integer fovAlly;
	private final Integer fovEnemy;
	private final Integer fovFood;
	private final Double desiredSeparation;

	/**
	 * constructor Prey extends Entity
	 */
	Prey(Double posX, Double posY, Double mass, Double massDecay, Double dir, Double turnRate, Integer fovAlly, Integer fovEnemy, Integer fovFood, Double desiredSeparation, Simulation sim) {
		super(posX, posY, mass, massDecay, sim);
		this.vel = 2.0;
		this.dir = toRadians(dir);
		this.turnRate = turnRate;
		this.fovAlly = fovAlly;
		this.fovEnemy = fovEnemy;
		this.fovFood = fovFood;
		this.desiredSeparation = desiredSeparation;
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

	/**
	 * Method Move is used to add all movements of the object and turn it into one vector
	 */

	@Override
	public void move() {
		double avgDir, hunger = 1 + (5 / mass), runDiv, eatDiv, algnDiv, cohDiv, sepDiv;
		int div = 0;
		algnDiv = alignment();
		cohDiv = cohesion();
		sepDiv = separation();
		runDiv = run();
		eatDiv = eat();
		if (algnDiv != 0) div++;
		if (cohDiv != 0) div++;
		if (sepDiv != 0) div += 100;
		if (runDiv != 0) div++;
		if (eatDiv != 0) div += hunger;
		if (div > 0) {
			avgDir = algnDiv + cohDiv + 100 * sepDiv + hunger * eatDiv + runDiv;
			avgDir /= div;
			dir += avgDir;
		}
		posX += cos(dir) * vel;
		posY += sin(dir) * vel;
		breed();
		mass -= massDecay;
		if (mass < 0) {
			this.die();
			System.out.println("Prey starved!");
		}
	}

	/**
	 * Method Alignment is used to steer entities towards average direction of the group
	 */
	double alignment() {
		double avgDir = 0.0, d;
		int i, total = 0;

		for (i = 0; i < this.sim.entities.size(); i++) {
			d = Math.sqrt(((this.posX - this.sim.entities.get(i).posX) * (this.posX - this.sim.entities.get(i).posX)) + ((this.posY - this.sim.entities.get(i).posY) * (this.posY - this.sim.entities.get(i).posY)));
			if (this.sim.entities.get(i) != this && d < fovAlly && this.sim.entities.get(i).getClass().getName().equals("sim.Prey")) {
				avgDir += this.sim.entities.get(i).dir;
				total++;
			}
		}
		if (total > 0) {
			avgDir /= total;
		}
		avgDir -= this.dir;
		if (avgDir > turnRate) avgDir = turnRate;
		else if (avgDir < -turnRate) avgDir = -turnRate;
		return avgDir;
	}

	/**
	 * Method Breed is used to add new objects based on their current mass
	 */
	void breed() {
		Random rand = new Random();
		if (this.mass >= 4) {
			this.sim.add(new Prey(this.posX + 1, this.posY + 1, 1.0, this.massDecay, rand.nextDouble() * Math.PI * 2, Math.PI / 4, this.fovAlly, this.fovEnemy, this.fovFood, this.desiredSeparation, this.sim));
			this.mass -= 1;
		}
	}

	/**
	 * Method Cohesion is used to steer entities towards average location of the group
	 */
	double cohesion() {
		int i, total = 0;
		double a, b, d, avgDir = 0.0;
		Vector avgPos = new Vector(0, 0);
		for (i = 0; i < this.sim.entities.size(); i++) {
			d = Math.sqrt(((this.posX - this.sim.entities.get(i).posX) * (this.posX - this.sim.entities.get(i).posX)) + ((this.posY - this.sim.entities.get(i).posY) * (this.posY - this.sim.entities.get(i).posY)));
			if (this.sim.entities.get(i) != this && d < fovAlly && this.sim.entities.get(i).getClass().getName().equals("sim.Prey")) {
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
			if (a > 0) avgDir = atan(b / a);
			else if (a < 0) avgDir = atan((b / a)) + Math.PI;
			avgDir -= dir;

			if (avgDir > turnRate) avgDir = turnRate;
			else if (avgDir < -turnRate) avgDir = -turnRate;

		}
		return avgDir;

	}

	/**
	 * Method Eat is used to navigate objects towards food
	 */
	double eat() {
		int i, id = -1;
		double d, minD = 300 + 1.0, a, b, avgDir = 0.0;
		Vector avgPos = new Vector(0, 0);


		for (i = 0; i < this.sim.entities.size(); i++) {

			d = Math.sqrt(Math.pow(this.posX - this.sim.entities.get(i).posX, 2) + Math.pow(this.posY - this.sim.entities.get(i).posY, 2));
			if (d < this.fovFood && this.sim.entities.get(i).getClass().getName().equals("sim.Food")) {
				if (d < minD) {
					minD = d;
					id = i;
				}
			}
		}
		if (id >= 0) {
			if (abs(this.posX - this.sim.entities.get(id).posX) < 4 && abs(this.posY - this.sim.entities.get(id).posY) < 4) {
				this.mass += this.sim.entities.get(id).mass;
				this.sim.entities.get(id).die();

			} else {
				avgPos.x = this.sim.entities.get(id).posX;
				avgPos.y = this.sim.entities.get(id).posY;
				a = avgPos.x - posX;
				b = avgPos.y - posY;
				if (a > 0) avgDir = atan(b / a);
				else if (a < 0) avgDir = atan(b / a) + Math.PI;
				avgDir -= dir;

				if (avgDir > turnRate) avgDir = turnRate;
				else if (avgDir < -turnRate) avgDir = -turnRate;
			}
		}
		return avgDir;
	}

	/**
	 * Method Run is used to navigate objects to run from the predators
	 */
	double run() {
		int i, total = 0;
		double a, b, d, avgDir = 0.0;
		Vector diff = new Vector(0, 0);
		Vector avgPos = new Vector(0, 0);
		for (i = 0; i < this.sim.entities.size(); i++) {
			d = Math.sqrt(Math.pow(this.posX - this.sim.entities.get(i).posX, 2) + Math.pow(this.posY - this.sim.entities.get(i).posY, 2));
			if (this.sim.entities.get(i) != this && d < fovEnemy && this.sim.entities.get(i).getClass().getName().equals("sim.Predator")) {
				diff.x = posX - this.sim.entities.get(i).posX;
				diff.y = posY - this.sim.entities.get(i).posY;
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
			if (a > 0) avgDir = atan(b / a);
			else if (a < 0) avgDir = atan((b / a)) + Math.PI;
			avgDir -= dir;

			if (avgDir > turnRate) avgDir = turnRate;
			else if (avgDir < -turnRate) avgDir = -turnRate;


		}
		return avgDir;
	}

	/**
	 * Method Separation is used to steer entities to the opposite direction than the average location of the group to avoid crowding
	 */
	double separation() {
		Vector avgPos = new Vector(0, 0);
		Vector diff = new Vector(0, 0);
		int total = 0, i;
		double d, a, b, avgDir = 0.0;
		for (i = 0; i < this.sim.entities.size(); i++) {
			d = Math.sqrt(((this.posX - this.sim.entities.get(i).posX) * (this.posX - this.sim.entities.get(i).posX)) + ((this.posY - this.sim.entities.get(i).posY) * (this.posY - this.sim.entities.get(i).posY)));
			if (this.sim.entities.get(i) != this && d < this.desiredSeparation && this.sim.entities.get(i).getClass().getName().equals("sim.Prey")) {
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
			if (a > 0) avgDir = atan(b / a);
			else if (a < 0) avgDir = atan((b / a)) + Math.PI;
			avgDir -= dir;

			if (avgDir > turnRate) avgDir = turnRate;
			else if (avgDir < -turnRate) avgDir = -turnRate;

		}


		return avgDir;
	}
}
