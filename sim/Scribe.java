package sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import static sim.Simulation.Feeder;

public class Scribe {
    private FileWriter writer;

    Scribe() {
        try {
            System.out.println(LocalDate.now());
            this.writer = new FileWriter("data_out\\" + LocalDate.now() + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to initialize a file!");
            System.exit(1);
        }
    }

    public void write(String s) {
        try {
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Simulation simInit(String source) {
        Simulation s = null;
        try {
            Scanner scanner = new Scanner(new File(source));
            s = new Simulation(scanner.nextBoolean(), scanner.nextDouble(), scanner.nextInt(), this);
            s.feeder = new Feeder(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble(), scanner.nextInt());
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                System.out.println("New Entity!");
                String newcomer = scanner.nextLine();
                System.out.println(newcomer);
                Scanner scanLine = new Scanner(newcomer);
                if (scanLine.next().equals("Prey")) {
                    System.out.println("Adding prey!");
                    s.add(new Prey(scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextInt(), scanLine.nextInt(), scanLine.nextInt()));
                }
                if (scanLine.next().equals("Predator")) {
                    s.add(new Prey(scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextInt(), scanLine.nextInt(), scanLine.nextInt()));
                }
                if (scanLine.next().equals("Food")) {
                    s.add(new Food(scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble(), scanLine.nextDouble()));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return s;

    
    }

    public void fin() {
        try {
            writer.close();
            System.out.println("Finalized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}